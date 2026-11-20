/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.opmode;

import first.robot.Robot;
import org.wpilib.driverstation.XboxController;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;

@Teleop
public class MyTeleop extends PeriodicOpMode {
  private final Robot robot;

  private final XboxController driveController = new XboxController(0);

  /** The Robot instance is passed into the opmode via the constructor. */
  public MyTeleop(Robot robot) {
    this.robot = robot;
  }

  @Override
  public void periodic() {
    /* Called periodically (set time interval) while the robot is enabled. */
    robot.drive.arcadeDrive(-driveController.getLeftY(), driveController.getLeftX());

    if (driveController.getXButton()) {
      robot.setShootdexerThrottle(0.9, 0.75);
    } else if (driveController.getLeftBumperButton()) {
      robot.setShootdexerThrottle(0.8, -1.0);
    } else if (driveController.getAButton()) {
      robot.setShootdexerThrottle(-0.8, 1.0);
    } else {
      robot.stopShootdexer();
    }
  }
}
