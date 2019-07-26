package siri_xlite.model;

import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

public class DestinationBuilder {

    public static Document create(String destinationRef, String placeName) {

        Document result = new Document("_id", destinationRef);
        append(result, "destinationRef", destinationRef);
        append(result, "placeName", placeName);

        return result;
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public static class DocumentBuilder {
        private String destinationRef;
        private String placeName;

        DocumentBuilder() {
        }

        public DocumentBuilder destinationRef(String destinationRef) {
            this.destinationRef = destinationRef;
            return this;
        }

        public DocumentBuilder placeName(String placeName) {
            this.placeName = placeName;
            return this;
        }

        public Document build() {
            return DestinationBuilder.create(destinationRef, placeName);
        }

        public String toString() {
            return "DestinationBuilder.DocumentBuilder(destinationRef=" + this.destinationRef + ", placeName="
                    + this.placeName + ")";
        }
    }
}
