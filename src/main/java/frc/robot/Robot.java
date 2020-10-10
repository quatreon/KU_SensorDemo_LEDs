/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
   private AddressableLED ledCircle;
   private AddressableLEDBuffer ledBuffer;

   //Sensor ports
   private int switchPort = 0;
   private int vexUltrasonicInput = 1;
   private int vexUltrasonicOutput = 2;
   private int potPort = 0;

   //LED Colors
   private int blue = 0;
   private int red = 0;
   private int green = 0;

   //Initialize Limit Switch
  private DigitalInput armSwitch = new DigitalInput(switchPort);
 
  //Initialize Potentiometer
  double fullRange = 360; //Convert into degrees, full range of Pot = 360 degrees   
  double offset = 15;    //Starting position at 0 V
  private AnalogPotentiometer armPot = new AnalogPotentiometer(potPort, fullRange, offset);
  
  //Initialize Accel 
  private BuiltInAccelerometer accel = new BuiltInAccelerometer(BuiltInAccelerometer.Range.k8G); 
  
  //Initialize Vex Ultrasonic
  private Ultrasonic vex_sonic = new Ultrasonic(vexUltrasonicInput, vexUltrasonicOutput);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    ledCircle = new AddressableLED(0);

    // Default to a length of 16, start empty output
    ledBuffer = new AddressableLEDBuffer(16);
    ledCircle.setLength(ledBuffer.getLength());

    // Set the data
    ledCircle.setData(ledBuffer);
    ledCircle.start();

     //Ultrasonic - Turn automatic ping mode
     vex_sonic.setAutomaticMode(true); 
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

   SmartDashboard.putBoolean("Switch Test", armSwitch.get());
   SmartDashboard.putNumber("Vex Ultra", vex_sonic.getRangeInches());
   SmartDashboard.putNumber("Pot Values", armPot.get());
   SmartDashboard.putNumber("Accel X", accel.getX());
   SmartDashboard.putNumber("Accel Y", accel.getX());
   SmartDashboard.putNumber("Accel Z", accel.getX());

  //Calculate Rotation around an axis
  //This can be used to detect a tipping robot
  double rot_x_rad = Math.atan2(accel.getY(), accel.getZ()); // Rotation about x-axis in radians
  double rot_y_rad = Math.atan2(accel.getX(), accel.getZ()); // Rotation about y-axis in radians
      
  double rot_x_deg = Math.toDegrees(rot_x_rad); // Rotation x-axis degrees
  double rot_y_deg = Math.toDegrees(rot_y_rad); // Rotation y-axis degrees
    
  //Display these values on smart dashboard
  SmartDashboard.putNumber("Tip X", rot_x_deg);
  SmartDashboard.putNumber("Tip Y", rot_y_deg); 

  if(rot_x_deg > 10 || rot_x_deg < -10){
    red = 0;
    green = 0;
    blue = 255;
  }
  else if(armSwitch.get()){ //switch
    red = 0;
    green = 0;
    blue = 0;
  }else{
    red = 0;
    green = 255;
    blue = 0;
  }

  red = (int)Math.round(red * (armPot.get()/370));
  green = (int)Math.round(green * (armPot.get()/370));
  blue = (int)Math.round(blue * (armPot.get()/370));

  for (var i = 0; i < ledBuffer.getLength(); i++) {
    // Sets the specified LED to the RGB values for red
    ledBuffer.setRGB(i, red, green, blue);
 }
 
 ledCircle.setData(ledBuffer);

}

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
