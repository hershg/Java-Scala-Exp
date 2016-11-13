package com.example.GyroSensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.sqrt;

public class GyroActivity extends Activity implements SensorEventListener {

    private TextView textView_accuracy;
    private TextView textView_value_0;
    private TextView textView_value_1;
    private TextView textView_value_2;

    private SensorManager sensorManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView_accuracy = (TextView) findViewById(R.id.textView_accuracy);
        textView_value_0 = (TextView) findViewById(R.id.textView_value_0);
        textView_value_1 = (TextView) findViewById(R.id.textView_value_1);
        textView_value_2 = (TextView) findViewById(R.id.textView_value_2);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(
                Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            switch (accuracy) {
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:

                    textView_accuracy.setText("Gyro: _ACCURACY_HIGH");

                    break;

                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:

                    textView_accuracy.setText("Gyro: _ACCURACY_MEDIUM");

                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:

                    textView_accuracy.setText("Gyro: _ACCURACY_LOW");

                    break;

            }
        }
    }

    /***
     Sensor.TYPE_GYROSCOPE:

     http://developer.android.com/reference/android/hardware/SensorEvent.html#values

     All values are in radians/second and measure the rate of rotation around the device's local X, Y and Z axis. The coordinate system is the same as is used for the acceleration sensor. Rotation is positive in the counter-clockwise direction. That is, an observer looking from some positive location on the x, y or z axis at a device positioned on the origin would report positive rotation if the device appeared to be rotating counter clockwise. Note that this is the standard mathematical definition of positive rotation and does not agree with the definition of roll given earlier.
     values[0]: Angular speed around the x-axis
     values[1]: Angular speed around the y-axis
     values[2]: Angular speed around the z-axis
     Typically the output of the gyroscope is integrated over time to calculate a rotation describing the change of angles over the timestep, for example:

     yaw, pitch and roll used in aviation where the X axis is along the long side of the plane (tail to nose).

     */
//    public static final float EPSILON = 0.000000001f;
    public static final float EPSILON = 0.001000000f;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.accuracy==SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }


        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = sensorEvent.values[0];
            float axisY = sensorEvent.values[1];
            float axisZ = sensorEvent.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            if (omegaMagnitude > EPSILON) {
                textView_value_0.setText( "Angular speed around  X - long side of Airplane - (Roll):" + Float.toString(sensorEvent.values[0]));
                textView_value_1.setText( "Angular speed around  Y - turn left or right+ - (Yaw):" + Float.toString(sensorEvent.values[1]));
                textView_value_2.setText( "Angular speed around  Z - turn down or up+ - (pitch):" + Float.toString(sensorEvent.values[2]));

                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = sin(thetaOverTwo);
            float cosThetaOverTwo = cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = sensorEvent.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;

    }
}
