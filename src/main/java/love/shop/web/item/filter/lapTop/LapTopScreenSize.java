package love.shop.web.item.filter.lapTop;

public enum LapTopScreenSize {
    Inch_17("17인치"), Inch_16("16인치"), Inch_15("15인치"), Inch_13("13인치");

    private final String screenSize;


    LapTopScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }


    @Override
    public String toString() {
        return screenSize;
    }
}
