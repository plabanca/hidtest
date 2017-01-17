# hidtest
Example Java 8 gradle Application that uses purejavahidapi to read Yubikey USB device

## Add udev filter to avoid running jar as sudo on Linux systems

```
$ cat /etc/udev/rules.d/40-scratch.rules
ATTRS{idVendor}=="16c0", ATTRS{idProduct}=="0486", SUBSYSTEMS=="usb", ACTION=="add", MODE="0666", GROUP="plugdev"
ATTRS{idVendor}=="1050", ATTRS{idProduct}=="0010", SUBSYSTEMS=="usb", ACTION=="add", MODE="0666", GROUP="plugdev"
```
## Build

```
$ git clone https://github.com/plabanca/hidtest 
$ cd hidtest
$ gradle jar
```

## Execute
```
$ java -jar builds/libs/hidtest.jar
```
