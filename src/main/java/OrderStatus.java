public enum OrderStatus {
    PROCESSING("processing"), IN_DELIVERY("in delivery"), COMPLETED("completed");
    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
