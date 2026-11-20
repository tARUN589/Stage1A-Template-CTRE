/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import first.robot.simulation.DrivetrainSim;
import first.robot.simulation.FuelSim;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.drive.DifferentialDrive;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.hardware.bus.CANPort;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot {
  private final CANBus CAN_BUS_0 = new CANBus(CANPort.CAN_S0);
  private final CANBus CAN_Bus_2 = new CANBus(CANPort.CAN_S2);

  private final TalonFX leftLeader = new TalonFX(0, CAN_Bus_2);
  private final TalonFX leftFollower = new TalonFX(1, CAN_Bus_2);
  private final TalonFX rightLeader = new TalonFX(2, CAN_Bus_2);
  private final TalonFX rightFollower = new TalonFX(3, CAN_Bus_2);

  public final TalonFX intakeShooter = new TalonFX(4, CAN_BUS_0);
  public final TalonFX feeder = new TalonFX(5, CAN_BUS_0);

  private final Pigeon2 pigeon = new Pigeon2(0, CAN_Bus_2);

  public final DifferentialDrive drive =
      new DifferentialDrive(leftLeader::setThrottle, rightLeader::setThrottle);

  public final DrivetrainSim driveSim = new DrivetrainSim(leftLeader, rightLeader);
  public final SingleFlywheelSim shooterSim = SingleFlywheelSim.forIntakeLauncher(intakeShooter);
  public final SingleFlywheelSim feederSim = SingleFlywheelSim.forFeeder(feeder);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    TalonFXConfiguration leftConfig = new TalonFXConfiguration();
    TalonFXConfiguration rightConfig = new TalonFXConfiguration();

    leftConfig.MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive);
    rightConfig.MotorOutput.withInverted(InvertedValue.Clockwise_Positive);

    leftLeader.getConfigurator().apply(leftConfig);
    leftFollower.getConfigurator().apply(leftConfig);
    leftFollower.setControl(new Follower(leftLeader.getDeviceID(), MotorAlignmentValue.Aligned));

    rightLeader.getConfigurator().apply(rightConfig);
    rightFollower.getConfigurator().apply(rightConfig);
    rightFollower.setControl(new Follower(rightLeader.getDeviceID(), MotorAlignmentValue.Aligned));
  }

  public void setShootdexerThrottle(double intakeShooterThrottle, double feederThrottle) {
    intakeShooter.setThrottle(intakeShooterThrottle);
    feeder.setThrottle(feederThrottle);
  }

  public void stopShootdexer() {
    setShootdexerThrottle(0, 0);
  }

  public void stopDrivetrain() {
    drive.arcadeDrive(0, 0);
  }

  @Override
  public void simulationPeriodic() {
    driveSim.periodic();
    shooterSim.periodic();
    feederSim.periodic();
    FuelSim.periodic();
  }
}
