/*
 * This Java source file was auto generated by running 'gradle buildInit --type java-library'
 * by 'phillip' at '12/20/16 11:57 PM' with Gradle 3.2.1
 *
 * @author phillip, @date 12/20/16 11:57 PM
 */

import java.util.List;
import purejavahidapi.*;

public class Application {

    volatile static boolean deviceOpen = false;

    private static String toHexString(short value) {
        return Integer.toHexString(value & 0xffff);
    }

    public static HidDeviceInfo enumerateDevices(short vendorId, short productId) {

        HidDeviceInfo devInfo = null;

        try {

            List<HidDeviceInfo> hidDeviceList = PureJavaHidApi.enumerateDevices();

            for (HidDeviceInfo info : hidDeviceList) {
                StringBuilder msg = new StringBuilder();

                msg.append("HID Device: ");
                msg.append(info.getDeviceId());

                msg.append("\n\tVID: ");
                msg.append(toHexString(info.getVendorId()));

                msg.append("\n\tPID: ");
                msg.append(toHexString(info.getProductId()));

                msg.append("\n\tManufacturer: ");
                msg.append(info.getManufacturerString());

                msg.append("\n\tProductString: ");
                msg.append(info.getProductString());

                msg.append("\n\tReleaseNumber: ");
                msg.append(toHexString(info.getReleaseNumber()));

                msg.append("\n\tPath: ");
                msg.append(info.getPath());

                msg.append("\n\tSerialNumber: ");
                msg.append(info.getSerialNumberString());

                msg.append("\n\tUsagePage: ");
                msg.append(toHexString(info.getUsagePage()));

                System.out.println(msg.toString());

                if (info.getVendorId() == vendorId && info.getProductId() == productId) {
                    devInfo = info;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devInfo;
    }

    public static void main(String[] args){

        try {

            while (true) {

                // Bus 002 Device 025: ID 1050:0010 Yubico.com Yubikey
                // Bus 002 Device 026: ID 16c0:0486 Van Ooijen Technische Informatica Teensyduino RawHID

                final short VENDOR_ID  = 0x1050;
                final short PRODUCT_ID = 0x0010;
                HidDevice dev = null;

                if (deviceOpen) {

                    Thread.sleep(100);

                } else {

                    HidDeviceInfo devInfo = enumerateDevices(VENDOR_ID, PRODUCT_ID);

                    if (devInfo == null) {

                        System.out.printf("hidraw device %x:%x not found\n", VENDOR_ID, PRODUCT_ID);
                        Thread.sleep(1000);

                    } else {

                        System.out.println("enumerateDevices() returns success!");

                        dev = PureJavaHidApi.openDevice(devInfo);
                        if (dev != null) {

                            deviceOpen = true;

                            dev.setDeviceRemovalListener(new DeviceRemovalListener() {
                                @Override
                                public void onDeviceRemoval(HidDevice source) {
                                    System.out.println("device removed");
                                    deviceOpen = false;
                                }
                            });

                            dev.setInputReportListener(new InputReportListener() {
                                @Override
                                public void onInputReport(HidDevice source, byte Id, byte[] data, int len) {
                                    System.out.printf("\tonInputReport: id %d len %d data ", Id, len);
                                    for (int i = 0; i < len; i++)
                                        System.out.printf("%02X ", data[i]);
                                    System.out.println();
                                }
                            });
                        }

                    }

                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
