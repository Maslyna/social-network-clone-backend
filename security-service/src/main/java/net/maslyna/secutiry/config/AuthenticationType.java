package net.maslyna.secutiry.config;

public enum AuthenticationType {

    BEARER {
        public String prefix() {
            return "Bearer ";
        }
    },
    BASIC {
        public String prefix() {
            return "Basic ";
        }
    };

    public abstract String prefix();

}
