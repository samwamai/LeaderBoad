package com.gads.leaderboad;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smartnovate.smartnetwork.Network.Speedtest.core.Speedtest;
import com.smartnovate.smartnetwork.Network.Speedtest.core.config.SpeedtestConfig;
import com.smartnovate.smartnetwork.Network.Speedtest.core.config.TelemetryConfig;
import com.smartnovate.smartnetwork.Network.Speedtest.core.serverSelector.TestPoint;
import com.smartnovate.smartnetwork.Network.Speedtest.ui.GaugeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class NetworkFragment extends Fragment {
    private NetworkViewModel mViewModel;
    private View view;
    View intiPage,page_error,pageTestArea=null;
    private String download_speed,upload_speed,ip_information=null;
    private Context context=getActivity();

    public static com.smartnovate.smartnetwork.Network.NetworkFragment newInstance() {
        return new com.smartnovate.smartnetwork.Network.NetworkFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.network_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        // TODO: Use the ViewModel

    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page_init();

    }

    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    private boolean reinitOnResume=false;

    @Override
    public void onDetach() {
        super.onDetach();
//        if(reinitOnResume){
//            reinitOnResume=false;
//            page_init();
//        }
        try{st.abort();}catch (Throwable t){}

    }






    private static Speedtest st=null;

    private void page_init(){
        new Thread(){
            @Override
            public void run() {
                intiPage=view.findViewById(R.id.page_init);
                pageTestArea=view.findViewById(R.id.page_test);
                page_error=view.findViewById(R.id.page_fail);


                final TextView t=view.findViewById(R.id.init_text);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        hideView(page_error.getId());
                        hideView(pageTestArea.getId());
                        showView(intiPage.getId());
                        t.setText(R.string.init_loading);
                    }
                });

                SpeedtestConfig config=null;
                TelemetryConfig telemetryConfig=null;
                TestPoint[] servers=null;
                try{
                    String c=readFileFromAssets("SpeedtestConfig.json");
                    JSONObject o=new JSONObject(c);
                    config=new SpeedtestConfig(o);
                    c=readFileFromAssets("TelemetryConfig.json");
                    o=new JSONObject(c);
                    telemetryConfig=new TelemetryConfig(o);
                    if(telemetryConfig.getTelemetryLevel().equals(TelemetryConfig.LEVEL_DISABLED)){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // hideView(R.id.privacy_open);
                            }
                        });
                    }
                    if(st!=null){
                        try{st.abort();}catch (Throwable e){}
                    }
                    st=new Speedtest();
                    st.setSpeedtestConfig(config);
                    st.setTelemetryConfig(telemetryConfig);
                    c=readFileFromAssets("ServerList.json");
                    if(c.startsWith("\"")||c.startsWith("'")){ //fetch server list from URL
                        if(!st.loadServerList(c.subSequence(1,c.length()-1).toString())){
                            throw new Exception("Failed to load server list");
                        }
                    }else{ //use provided server list
                        JSONArray a=new JSONArray(c);
                        if(a.length()==0) throw new Exception("No test points");
                        ArrayList<TestPoint> s=new ArrayList<>();
                        for(int i=0;i<a.length();i++) s.add(new TestPoint(a.getJSONObject(i)));
                        servers=s.toArray(new TestPoint[0]);
                        st.addTestPoints(servers);
                    }
                    final String testOrder=config.getTest_order();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!testOrder.contains("D")){
                                hideView(R.id.dlArea);
                            }
                            if(!testOrder.contains("U")){
                                hideView(R.id.ulArea);
                            }
                            if(!testOrder.contains("P")){
                                //hideView(R.id.pingArea);
                            }
                            if(!testOrder.contains("I")){
                                hideView(R.id.ipInfo);
                            }
                        }
                    });
                }catch (final Throwable e){
                    System.err.println(e);
                    st=null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideView(intiPage.getId());
                            hideView(pageTestArea.getId());
                            showView(page_error.getId());

                            TextView textView=view.findViewById(R.id.fail_text);
                            textView.setText(getString(R.string.initFail_configError)+": "+e.getMessage());
                            final Button b=(Button)view.findViewById(R.id.fail_button);
                            b.setText(R.string.initFail_retry);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideView(intiPage.getId());
                                    hideView(pageTestArea.getId());
                                    hideView(page_error.getId());
                                    page_init();
                                    b.setOnClickListener(null);
                                }
                            });
                        }
                    });
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        t.setText(R.string.init_selecting);
                    }
                });
                st.selectServer(new Speedtest.ServerSelectedHandler() {
                    @Override
                    public void onServerSelected(final TestPoint server) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if(server==null){
                                    hideView(intiPage.getId());
                                    hideView(pageTestArea.getId());
                                    showView(page_error.getId());

                                    TextView textView=view.findViewById(R.id.fail_text);
                                    textView.setText(getString(R.string.initFail_noServers));
                                    final Button b=(Button)view.findViewById(R.id.fail_button);
                                    b.setText(R.string.initFail_retry);
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hideView(intiPage.getId());
                                            hideView(pageTestArea.getId());
                                            hideView(page_error.getId());
                                            page_init();
                                            b.setOnClickListener(null);
                                        }
                                    });
                                }else{
                                    page_serverSelect(server,st.getTestPoints());
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }

    private void page_serverSelect(TestPoint selected, TestPoint[] servers){
        reinitOnResume=true;
        final ArrayList<TestPoint> availableServers=new ArrayList<>();
        for(TestPoint t:servers) {
            if (t.getPing() != -1) availableServers.add(t);
        }
        ArrayList<String> options=new ArrayList<String>();
        for(TestPoint t:availableServers){
            options.add(t.getName());
        }

        reinitOnResume=false;
        page_test(availableServers.get(0));
    }



    private void page_test(final TestPoint selected){
        st.setSelectedServer(selected);
        ((TextView)view.findViewById(R.id.dlText)).setText(format(0));
        ((TextView)view.findViewById(R.id.ulText)).setText(format(0));
        ((ProgressBar)view.findViewById(R.id.dlProgress)).setProgress(0);
        ((ProgressBar)view.findViewById(R.id.ulProgress)).setProgress(0);
        ((GaugeView)view.findViewById(R.id.dlGauge)).setValue(0);
        ((GaugeView)view.findViewById(R.id.ulGauge)).setValue(0);

        st.start(new Speedtest.SpeedtestHandler() {
            @Override
            public void onDownloadUpdate(final double dl, final double progress) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        download_speed=format(dl);

                        ((TextView)view.findViewById(R.id.dlText)).setText(progress==0?"...": format(dl));
                        ((GaugeView)view.findViewById(R.id.dlGauge)).setValue(progress==0?0:mbpsToGauge(dl));
                        ((ProgressBar)view.findViewById(R.id.dlProgress)).setProgress((int)(100*progress));
                    }
                });
            }

            @Override
            public void onUploadUpdate(final double ul, final double progress) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        upload_speed=format(ul);

                        ((TextView)view.findViewById(R.id.ulText)).setText(progress==0?"...": format(ul));
                        ((GaugeView)view.findViewById(R.id.ulGauge)).setValue(progress==0?0:mbpsToGauge(ul));
                        ((ProgressBar)view.findViewById(R.id.ulProgress)).setProgress((int)(100*progress));
                    }
                });

            }

            @Override
            public void onPingJitterUpdate(final double ping, final double jitter, final double progress) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ((TextView)findViewById(R.id.pingText)).setText(progress==0?"...": format(ping));
//                        ((TextView)findViewById(R.id.jitterText)).setText(progress==0?"...": format(jitter));
                    }
                });
            }

            @Override
            public void onIPInfoUpdate(final String ipInfo) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideView(page_error.getId());
                        hideView(intiPage.getId());
                        showView(pageTestArea.getId());

                        ip_information=null;
                        ip_information=ipInfo;

                        ((TextView)view.findViewById(R.id.textViewPublicIpIn)).setText(ipInfo);


                    }
                });
            }

            @Override
            public void onTestIDReceived(final String id, final String shareURL) {
                if(shareURL==null||shareURL.isEmpty()||id==null||id.isEmpty()) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onEnd() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        final Button restartButton=(Button)view.findViewById(R.id.restartButton);
//                        restartButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                page_init();
//                                restartButton.setOnClickListener(null);
//                            }
//                        });
                        getWifiInfo(view,getActivity());

                    }
                });
            }

            @Override
            public void onCriticalFailure(String err) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideView(intiPage.getId());
                        hideView(pageTestArea.getId());

                        ((TextView)view.findViewById(R.id.fail_text)).setText(getString(R.string.testFail_err));
                        final Button b=(Button)view.findViewById(R.id.fail_button);
                        b.setText(R.string.testFail_retry);
                        showView(page_error.getId());
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideView(intiPage.getId());
                                hideView(pageTestArea.getId());
                                hideView(page_error.getId());
                                page_init();
                                b.setOnClickListener(null);
                            }
                        });
                    }
                });
            }
        });
    }

    private String format(double d){
        Locale l=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            l = getResources().getConfiguration().getLocales().get(0);
        }else{
            l=getResources().getConfiguration().locale;
        }
        if(d<10) return String.format(l,"%.2f",d);
        if(d<100) return String.format(l,"%.1f",d);
        return ""+Math.round(d);
    }

    private int mbpsToGauge(double s){
        return (int)(1000*(1-(1/(Math.pow(1.3,Math.sqrt(s))))));
    }

    private String readFileFromAssets(String name) throws Exception{
        BufferedReader b=new BufferedReader(new InputStreamReader(getActivity().getAssets().open(name)));
        String ret="";
        try{
            for(;;){
                String s=b.readLine();
                if(s==null) break;
                ret+=s;
            }
        }catch(EOFException e){}
        return ret;
    }

    private void hideView(int id){
        View v=view.findViewById(id);
        if(v!=null) v.setVisibility(View.INVISIBLE);
    }
    private void showView(int id){
        View v=view.findViewById(id);
        if(v!=null) v.setVisibility(View.VISIBLE);
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public  String getWifiInfo(View view,Context context) {
        String ssid,linkspeeed,macaddress,gatewayip,dns1,dns2,netmask = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
                //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
                WifiInfo info = wifiManager.getConnectionInfo();
                ssid = info.getSSID();
                linkspeeed= String.valueOf(info.getLinkSpeed())+"Mbps";
                macaddress=info.getMacAddress();
                gatewayip=getIpFromIntSigned(wifiManager.getDhcpInfo().gateway);
                dns1= getIpFromIntSigned(wifiManager.getDhcpInfo().dns1);
                dns2= getIpFromIntSigned(wifiManager.getDhcpInfo().dns2);
                netmask = getIpFromIntSigned(wifiManager.getDhcpInfo().netmask);
                String BSSID = info.getBSSID().toUpperCase();
                String broadcastIp = getIpFromIntSigned((wifiManager.getDhcpInfo().ipAddress & wifiManager.getDhcpInfo().netmask));


                log(ssid+linkspeeed+macaddress+gatewayip+dns1+dns2+netmask);


                ((TextView)view.findViewById(R.id.textViewLanIn)).setText(broadcastIp);

                ((TextView)view.findViewById(R.id.textViewWifiIn)).setText(ssid);
                ((TextView)view.findViewById(R.id.textViewLinkSpeedin)).setText(linkspeeed);
                ((TextView)view.findViewById(R.id.textViewMacin)).setText(BSSID);
                ((TextView)view.findViewById(R.id.textViewRouterIn)).setText(gatewayip);
                ((TextView)view.findViewById(R.id.textViewDNS1in)).setText(dns1);
                ((TextView)view.findViewById(R.id.textViewDNS2in)).setText(dns2);
                ((TextView)view.findViewById(R.id.textViewNetmaskin)).setText(netmask);
                ((TextView)view.findViewById(R.id.textViewSpeedtestDowloadin)).setText(download_speed);
                ((TextView)view.findViewById(R.id.textViewSpeedtestUploadin)).setText(upload_speed);


                return ssid;
            }
        }
        return "";
    }

    public static String getIpFromIntSigned(int ip_int) {
        String ip = "";
        for (int k = 0; k < 4; k++) {
            ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }

    private void log(String sms){
        Log.e(TAG, sms);
    }

}