Desktop Installation Instructions
=================================
Download and install opencv. Run the jar using the command:

java -Djava.library.path=/path/to/your/OpenCV/java/ -jar Ohm-Desktop-1.0.jar


We're having issues running because of some file io exceptions to do with the locations of the training data. You can build from gradle by simply putting in your opencv paths and using gradle runOhm.