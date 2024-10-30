package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "mecanumFullaccelUSETHIS 2 (Blocks to Java)")
public class mecanumFullaccelUSETHIS2 extends LinearOpMode {

  private Servo claw;
  private CRServo sbintake;
  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor backLeft;
  private DcMotor backRight;
  private DcMotor arm;
  private DcMotor wrist;

  float rsy;
  float rsx;
  float outLeftStickX = 0;
  float outRightStickX = 0;
  double timeVar = System.currentTimeMillis();
  float outLeftStickY = 0;
  float strafeInput = 0;
  float accelAdjust = 0.02f;
  float topSpeed = 0.6f;

  /**
   * Describe this function...
   */
  private void MOTOR_SETTINGS() {
    claw.setDirection(Servo.Direction.REVERSE);
    sbintake.setDirection(CRServo.Direction.REVERSE);
    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontLeft.setDirection(DcMotor.Direction.FORWARD);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setDirection(DcMotor.Direction.FORWARD);
    backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backLeft.setDirection(DcMotor.Direction.REVERSE);
    backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backRight.setDirection(DcMotor.Direction.REVERSE);
  }

  /**
   * Describe this function...
   */
  private void setTowerPower() {
    if (gamepad1.right_stick_y > 0) {
      rsy = gamepad1.right_stick_y - 1000;
    } else {
      rsy = gamepad1.right_stick_y + 1000;
    }
  }

  /**
   * Describe this function...
   */
  private void setWristPower() {
    if (gamepad1.right_stick_x > 0) {
      rsx = gamepad1.right_stick_x - 100;
    } else {
      rsx = gamepad1.right_stick_x + 100;
    }
  }

  /**
   * This function is executed when this Op Mode is selected.
   */
  @Override
  public void runOpMode() {
    telemetry.addData("Program is running!", null);
    double errlsx;
    
    double outlsx;

    claw = hardwareMap.get(Servo.class, "claw");
    sbintake = hardwareMap.get(CRServo.class, "sb-intake");
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    backRight = hardwareMap.get(DcMotor.class, "backRight");
    arm = hardwareMap.get(DcMotor.class, "arm");
    wrist = hardwareMap.get(DcMotor.class, "wrist");

    // Put initialization blocks here.
    MOTOR_SETTINGS();
    
    waitForStart();
    if (opModeIsActive()) {
      
      while (opModeIsActive()) {
        telemetry.addData("Test", null);
        MECANUM_DRIVE();
        setTowerPower();
        setWristPower();
        
        telemetry.update();
        if (gamepad1.right_stick_y != 0) {
          ((DcMotorEx) arm).setVelocity(rsy);
        } else {
          ((DcMotorEx) arm).setVelocity(1);
        }
        if (gamepad1.right_stick_x != 0) {
          ((DcMotorEx) wrist).setVelocity(rsx);
        } else {
          ((DcMotorEx) wrist).setVelocity(0.1);
        }
        while (gamepad1.a) {
          sbintake.setPower(-1);
          backLeft.setPower(0);
          backRight.setPower(0);
        }
        while (gamepad1.b) {
          sbintake.setPower(1);
          frontLeft.setPower(0);
          frontRight.setPower(0);
        }
        sbintake.setPower(0);
        if (gamepad1.x) {
          claw.setPosition(0.55);
        }
        if (gamepad1.y) {
          claw.setPosition(0);
        }
      }
    }
  }

  /**
   * Describe this function...
   */
  private void MECANUM_DRIVE() {
    
    float errLeftStickY;
    
    float errLeftStickX;
    float errRightStickX;
    
    
    telemetry.addData(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis() > (timeVar + 50)));
    
    if (System.currentTimeMillis() > (timeVar + 50)) {
      telemetry.addData("If block is running!", null);
      telemetry.update();
      strafeInput = gamepad1.left_trigger - gamepad1.right_trigger;
      errLeftStickX = gamepad1.left_stick_x - outLeftStickX;
      
      if (errLeftStickX > accelAdjust) {
        outLeftStickX = (float) (outLeftStickX + accelAdjust);
      } else if (errLeftStickX < -accelAdjust) {
        outLeftStickX = (float) (outLeftStickX - accelAdjust);
      } else {
        outLeftStickX = gamepad1.left_stick_x;
      }
      errLeftStickY = gamepad1.left_stick_y -outLeftStickY;
      if (errLeftStickY > accelAdjust) {
        outLeftStickY = (float) (outLeftStickY + accelAdjust);
      } else if (errLeftStickY < -accelAdjust) {
        outLeftStickY = (float) (outLeftStickY - accelAdjust);
      } else {
        outLeftStickY = gamepad1.left_stick_y;
      }
      errRightStickX = strafeInput - outRightStickX;
      if (errRightStickX > accelAdjust) {
        outRightStickX = (float) (outRightStickX + accelAdjust);
      } else if (errRightStickX < -accelAdjust) {
        outRightStickX = (float) (outRightStickX - accelAdjust);
      } else {
        outRightStickX = strafeInput;
      }
      String errlsy = String.valueOf(errLeftStickY);
      telemetry.addData(errlsy, null);
      telemetry.update();
      timeVar = System.currentTimeMillis();
    }
    
    frontRight.setPower(-outLeftStickX + outRightStickX - outLeftStickY);
    backRight.setPower(-outLeftStickX - outRightStickX - outLeftStickY);
    frontLeft.setPower(outLeftStickX - outRightStickX - outLeftStickY);
    backLeft.setPower(outLeftStickX + outRightStickX - outLeftStickY);
    
  }
}
