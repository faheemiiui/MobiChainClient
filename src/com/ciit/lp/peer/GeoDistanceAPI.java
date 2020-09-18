package com.ciit.lp.peer;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.Coordinate;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

public class GeoDistanceAPI {

	public static void main(String[] args) {
		Coordinate lat = new DegreeCoordinate(48.154563);
		Coordinate lng = new DegreeCoordinate(17.072561);
		Point loc1 = new Point(lat, lng);

		lat = new DegreeCoordinate(48.154564);
		lng = new DegreeCoordinate(17.072562);
		Point loc2 = new Point(lat, lng);

		System.out.println("Bearing: "+EarthCalc.getBearing(loc1, loc2));
		double distance = EarthCalc.getVincentyDistance(loc1, loc2); //in meters
		System.out.println(distance);


		distance = EarthCalc.getHarvesineDistance(loc2, loc1); //in meters
		System.out.println(distance);

		//Distance away point, bearing is 45deg
//		Point otherPoint = EarthCalc.pointRadialDistance(loc1, 45, 1000);


		//Determining whether a Point is contained within a BoundingArea
		//Now say you have a BoundingArea,

		//somewhere in Europe, not sure where ;-)
		Point northEast = new Point(new DegreeCoordinate(70), new DegreeCoordinate(145));
		Point southWest = new Point(new DegreeCoordinate(50), new DegreeCoordinate(110));
		BoundingArea boundingArea = new BoundingArea(northEast, southWest);
		//		you can determine whether a point is contained withing that area using:

		Point point1 = new Point(new DegreeCoordinate(60), new DegreeCoordinate(120));
		System.out.println("Is within area:"+boundingArea.isContainedWithin(point1));

		Point point2 = new Point(new DegreeCoordinate(45), new DegreeCoordinate(120));
		System.out.println("Is within area:"+boundingArea.isContainedWithin(point2));

	}

}
