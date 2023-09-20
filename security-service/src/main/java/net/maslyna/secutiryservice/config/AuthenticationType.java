package net.maslyna.secutiryservice.config;

public enum AuthenticationType {

    BEARER {
        public String prefix() {
            return "Bearer ";
        }
    }, BASIC {
        public String prefix() {
            return "Basic ";
        }
    };

    public abstract String prefix();
}
