    package com.gads.leaderboad.Model;


    import com.google.gson.annotations.Expose;
    import com.google.gson.annotations.SerializedName;

    public class LeaderIQ {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("score")
        @Expose
        private String score;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("badgeUrl")
        @Expose
        private String badgeUrl;

        public LeaderIQ(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getBadgeUrl() {
            return badgeUrl;
        }

        public void setBadgeUrl(String badgeUrl) {
            this.badgeUrl = badgeUrl;
        }

        @Override
        public String toString() {
            return "LeaderIQ{" +
                    "name='" + name + '\'' +
                    ", skill='" + score + '\'' +
                    ", country='" + country + '\'' +
                    ", image=" + badgeUrl +
                    '}';
        }
    }
