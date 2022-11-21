package fr.caensup.lsts.smb116.thermometre;

public interface ITemperatureCaptor {
    int getTemperature();
    void reset();
    void calibrate();
    void test();
}
