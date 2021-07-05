    package com.gads.leaderboad.Model;

    public class Submit_info {

        private String First_Name;
        private String Last_Name;
        private String Link_to_project;
        private  String Email_Address;

        public String getEmail_Address() {
            return Email_Address;
        }

        public void setEmail_Address(String email_Address) {
            Email_Address = email_Address;
        }

        public String getFirst_Name() {
            return First_Name;
        }

        public void setFirst_Name(String first_Name) {
            First_Name = first_Name;
        }

        public String getLast_Name() {
            return Last_Name;
        }

        public void setLast_Name(String last_Name) {
            Last_Name = last_Name;
        }

        public String getLink_to_project() {
            return Link_to_project;
        }

        public void setLink_to_project(String link_to_project) {
            Link_to_project = link_to_project;
        }

        Submit_info(){
        }

        @Override
        public String toString() {
            return "Submit_info{" +
                    "First_Name='" + First_Name + '\'' +
                    ", Last_Name='" + Last_Name + '\'' +
                    ", Link_to_project='" + Link_to_project + '\'' +
                    ", Email_Address='" + Email_Address + '\'' +
                    '}';
        }



    }
