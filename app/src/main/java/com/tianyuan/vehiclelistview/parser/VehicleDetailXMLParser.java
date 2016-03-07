package com.tianyuan.vehiclelistview.parser;

import com.tianyuan.vehiclelistview.model.Structures;
import com.tianyuan.vehiclelistview.model.VehicleDetails;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by churyan on 16-03-06.
 */
public class VehicleDetailXMLParser {
    public static VehicleDetails parseDetails(String doc) {

        try {

            VehicleDetails details = new VehicleDetails();
            Structures structures = null;
            double price = 0.0;
            String description = "";
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(doc));

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = parser.getEventType();
                String name = parser.getName();
                if (eventType == XmlPullParser.START_TAG)
                    if (name.equals("Ad")) {
                        continue;
                    } else if (name.equals("Structures")) {
                        structures = new Structures();
                    } else if (name.equals("Structure")) {
                        if (structures != null) {
                            String type = parser.getAttributeValue(null, "Type");
                            String en = parser.getAttributeValue(null, "En");
                            if (type.equals("Make")) {
                                structures.setMake(en);
                            } else if (type.equals("Year")) {
                                structures.setYear(Integer.parseInt(en));
                            } else if (type.equals("Model")) {
                                structures.setModel(en);
                            }
                        }
                    }else if(name.equals("Prices")){
                        continue;
                    }
                    else if (name.equals("Price") && price == 0.0) {
                        String type = parser.getAttributeValue(null, "Type");
                        String val = parser.getAttributeValue(null, "Value");
                        if (type.equals("Price")) {
                            price = Double.parseDouble(val);
                            details.setPrice(price);
                        }
                    }
                    else if (name.equals("Resources")){
                        continue;
                    }
                    else if (name.equals("Resource")){
                        String type = parser.getAttributeValue(null, "Type");
                        String src = parser.getAttributeValue(null, "SourceUrl");
                        if (type.equals("MainPhoto")) {
                            details.setImageUrl(src);
                        }
                    }
                    else if (name.equals("Description")){
                        continue;
                    }
                    else if (name.equals("Default")){
                        if (parser.next() == XmlPullParser.TEXT) {
                            description = parser.getText();
                            parser.nextTag();
                        }
                    }
                    else {
                        skip(parser);
                    }
            }

            details.setStructures(structures);
            details.setDescription(description);
            return details;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
