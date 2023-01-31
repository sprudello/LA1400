package BurletJulius;
import robocode.*;
import java.lang.Math;

public class Sprudello extends JuniorRobot
{
	public int north = 0;
	//Defining variables for target info
	public int scannedDistance_;
	public int scannedVelocity_;
	public int gunHeading_;
	public int scannedAngle_;
	public int scannedHeading_;

	//Defining variables for calculations
	public int distanceTraveled;
	public int angleForCalc;
	public int beta;
	public int preAimAngle_;
	public int omegaPreAimAngle;
	public double timeForBullet;
	public double shootPath;
	public double preAimAngle;
	public double power = 2.6;

	public void run() {
		
		setColors(purple, white, white, purple, purple);
		
		driveOnTopOfMap();
		turnGunRight(360);
		int i = 0;
		
		while(true) {
			ahead(100);
			i++;
			if(i % 2 == 0){
				turnGunRight(360);
			}		
		}
	}
	

	public void onScannedRobot() {
		
		saveDisappearingData();
		calcPreAimAngle();
		outputInfosAboutCalculations();
		shoot();
		
	}

	public void onHitByBullet() {
		turnGunTo(hitByBulletAngle);
		ahead(50);
		back(50);
	}
		
	public void onHitWall() {
		turnRight(90);
	}	

	public void outputInfosAboutCalculations(){
		out.println("shootPath: " + shootPath);
		out.println("PreAimAngle: " + preAimAngle);
		out.println("gunHeading: " + gunHeading);
		out.println("omegaafter" + omegaPreAimAngle);
	}
	
		public void saveDisappearingData(){
		scannedDistance_ = scannedDistance;
		scannedVelocity_ = scannedVelocity;
		turnGunTo(scannedAngle);
		gunHeading_ = gunHeading;
		scannedAngle_ = gunHeading_;
		scannedHeading_ = scannedHeading;
	}

	public void bulletFlightTime(){
		timeForBullet = scannedDistance_ / (20 - (3 * power));
	}

	public void distanceTraveled(){
		distanceTraveled = scannedVelocity_ * (int)timeForBullet;
	}

	public void driveOnTopOfMap(){
		turnTo(north);
		ahead(fieldHeight - robotY);
	}

	public void outputInfosAboutTarget(){
		out.println("ScannedDistance: " + scannedDistance_);
		out.println("ScannedVelocity: " + scannedVelocity_);
		out.println("gunHeading: "  + gunHeading_);
	}

	public void calcBeta(){
		angleForCalc = scannedAngle_ - 180;
		beta = angleForCalc - scannedHeading_;
	}

	public void outputInfosAboutAngles(){
		out.println("DistanceSPreCalc: " + scannedDistance_);
		out.println("DistanceTPReCalc: " + distanceTraveled);
		out.println("BetaPreCalc: " + beta);
	}

	public void normalizePreAimAngle(){
		preAimAngle_ = (int)preAimAngle;
		omegaPreAimAngle = 0;

		//Checks if an angle isn't over 360° and if so, changes it that it's the right angle.
		if(scannedAngle_ + preAimAngle > 360){
			omegaPreAimAngle = scannedAngle_ + preAimAngle_ - 360;
		}
		else{
			omegaPreAimAngle = scannedAngle_ + preAimAngle_;
		}
	}
	
		public void calcPreAimAngle(){
		outputInfosAboutTarget();
		bulletFlightTime();
		distanceTraveled();
		calcBeta();
		outputInfosAboutAngles();
		//Calculates the length of the shooting path.
		shootPath =  Math.sqrt(Math.pow(scannedDistance_, 2) + Math.pow(distanceTraveled, 2) - 2 * scannedDistance_ * distanceTraveled * Math.cos(beta));
		//Should calculate gamma / the pre aim angle
		preAimAngle = Math.acos((Math.pow(scannedDistance_, 2) + Math.pow(shootPath, 2) - Math.pow(distanceTraveled, 2)) / (2*scannedDistance_*shootPath));
		normalizePreAimAngle();
	}

	public void shoot(){
		turnGunTo(omegaPreAimAngle);
		fire(2.6);
	}
}
