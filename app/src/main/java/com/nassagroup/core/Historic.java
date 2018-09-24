
        package com.nassagroup.core;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        public class Historic {

            @SerializedName("error")
            @Expose
            public boolean error;
            @SerializedName("log")
            @Expose
            public Log log;

        }