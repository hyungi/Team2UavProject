package team2gcs.appmain;
import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Electromagnet {
    private GpioPinDigitalOutput pin;

    public Electromagnet(Pin pinNo) {
        GpioController gpioController = GpioFactory.getInstance();
        pin = gpioController.provisionDigitalOutputPin(pinNo);
        pin.setShutdownOptions(true,PinState.HIGH);

    }

    public void setPin(boolean state) {
        if (state) {
            pin.high();
        }
        else {
            pin.low();
        }
    }

    public String getPin() {
        if(pin.isHigh())
            return "High";
        else
            return "Low";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Electromagnet electromagnet1 = new Electromagnet(RaspiPin.GPIO_28);
        Electromagnet electromagnet2 = new Electromagnet(RaspiPin.GPIO_29);

            electromagnet1.setPin(true);
            electromagnet2.setPin(true);

    }
}
