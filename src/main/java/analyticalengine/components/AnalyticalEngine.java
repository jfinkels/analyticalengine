package analyticalengine.components;

public interface AnalyticalEngine {
    void setAttendant(Attendant attendant);
    void setMill(Mill mill);
    void setStore(Store store);
    void setCardReader(CardReader reader);
    void setPrinter(Printer printer);
    void setCurvePrinter(CurvePrinter printer);
    void run();
}
