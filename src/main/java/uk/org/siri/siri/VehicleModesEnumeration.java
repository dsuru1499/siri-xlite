package uk.org.siri.siri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour VehicleModesEnumeration.
 *
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="VehicleModesEnumeration">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="air"/>
 *     &lt;enumeration value="bus"/>
 *     &lt;enumeration value="coach"/>
 *     &lt;enumeration value="ferry"/>
 *     &lt;enumeration value="metro"/>
 *     &lt;enumeration value="rail"/>
 *     &lt;enumeration value="tram"/>
 *     &lt;enumeration value="underground"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "VehicleModesEnumeration")
@XmlEnum
public enum VehicleModesEnumeration {

    @XmlEnumValue("air")
    AIR("air"), @XmlEnumValue("bus")
    BUS("bus"), @XmlEnumValue("coach")
    COACH("coach"), @XmlEnumValue("ferry")
    FERRY("ferry"), @XmlEnumValue("metro")
    METRO("metro"), @XmlEnumValue("rail")
    RAIL("rail"), @XmlEnumValue("tram")
    TRAM("tram"), @XmlEnumValue("underground")
    UNDERGROUND("underground");
    private final String value;

    VehicleModesEnumeration(String v) {
        value = v;
    }

    public static VehicleModesEnumeration fromValue(String v) {
        for (VehicleModesEnumeration c : VehicleModesEnumeration.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
