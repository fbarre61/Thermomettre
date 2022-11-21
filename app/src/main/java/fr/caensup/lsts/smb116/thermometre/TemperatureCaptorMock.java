package fr.caensup.lsts.smb116.thermometre;

public class TemperatureCaptorMock implements ITemperatureCaptor {
    private int meanTemperature = 25;
    private int temperature;
    private int higherTempRate = 52;

    public TemperatureCaptorMock(int meanTemp) {
        this.meanTemperature = meanTemp;
        temperature = meanTemperature;
    }

    @Override
    public int getTemperature() {
        int r = (int)(Math.random() * 100) + 1;
        if(r <= higherTempRate) {
            temperature++;
        }else {
            temperature--;
        }
        return temperature;
    }

    @Override
    public void reset() {

    }

    @Override
    public void calibrate() {

    }

    @Override
    public void test() {

    }
}
