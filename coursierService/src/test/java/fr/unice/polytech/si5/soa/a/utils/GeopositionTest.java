package fr.unice.polytech.si5.soa.a.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GeopositionTest {

    private double latitudeCoursier, longitudeCoursier, latitudeBelow10, longitudeBelow10, latitudeOver10, longitudeOver10;

    @BeforeEach
    public void setUp() {
        latitudeCoursier = 0.0;
        longitudeCoursier = 0.0;
        latitudeBelow10 = 0.05;
        longitudeBelow10 = 0.05;
        latitudeOver10 = 1.0;
        longitudeOver10 = 1.0;
    }

    @Test
    public void distanceBelow10Test() {
        assertTrue(Geoposition.distance(latitudeCoursier, latitudeBelow10, longitudeCoursier, longitudeBelow10) < Geoposition.DISTANCE_MAX_DELIVERY);
    }

    @Test
    public void distanceOver10Test() {
        assertFalse(Geoposition.distance(latitudeCoursier, latitudeOver10, longitudeCoursier, longitudeOver10) < Geoposition.DISTANCE_MAX_DELIVERY);
    }
}
