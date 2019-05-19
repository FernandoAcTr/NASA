package services.library;

import services.RequestException;
import services.Service;

import java.io.IOException;

public class LibraryCollection {
    private Collection collection;

    public LibraryCollection() {
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    /**
     * Separa Los enlaces en JSON por comas
     *
     * @param JSONResult
     * @return
     */
    private static String[] splitLinks(String JSONResult) {
        return JSONResult.split("\",");
    }

    private static String removeJSONCharacters(String secuence) {
        if (secuence.startsWith(" "))
            secuence = secuence.substring(1);

        String s = secuence.replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\"", "");

        return s;
    }

    /**
     * Clases internas para el tratamiento de los medios
     **/

    public static class Image {
        private String JSONResult;

        public Image(String href) throws IOException, RequestException {
            JSONResult = Service.getRequest(href);
        }

        public String getJSONResult() {
            return JSONResult;
        }

        /**
         * De la lista de links, regresa el que lleva a la imagen small
         *
         * @return
         */
        public String getLinkImage() {
            String[] links = splitLinks(JSONResult);

            for (String l : links)
                if (l.contains("small")) {
                    l = removeJSONCharacters(l);
                    return Service.removeCommonsCharacter(l).replaceAll("http", "https");
                    //System.out.println(removeJSONCharacters(l));
                }

            return null;
        }
    }

    public static class Video {
        private String JSONResult;

        public Video(String href) throws IOException, RequestException {
            JSONResult = Service.getRequest(href);
        }

        public String getJSONResult() {
            return JSONResult;
        }

        public String getLinkVideo() {
            String links[] = splitLinks(JSONResult);

            for (String l : links) {
                if (l.contains("medium.mp4")) {
                    l = removeJSONCharacters(l);
                    return Service.removeCommonsCharacter(l).replaceAll("http", "https");
                    //System.out.println(removeJSONCharacters(l));
                }
            }

            return null;
        }

        public String getLinkThumb(){
            String links[] = splitLinks(JSONResult);
            int count = 0;
            for (String l : links) {
                if (l.contains("thumb") && count > 2) {
                    l = removeJSONCharacters(l);
                    return Service.removeCommonsCharacter(l).replaceAll("http", "https");
                    //System.out.println(removeJSONCharacters(l));
                }
                count++;
            }

            return null;
        }
    }

    public static class Audio {
        private String JSONResult;

        public Audio(String href) throws IOException, RequestException {
            JSONResult = Service.getRequest(href);
        }

        public String getJSONResult() {
            return JSONResult;
        }

        public String getLinkAudio() {
            String links[] = splitLinks(JSONResult);

            for (String l : links) {
                if (l.contains("128k.mp3")) {
                    l = removeJSONCharacters(l);
                    return Service.removeCommonsCharacter(l).replaceAll("http", "https");
                    //System.out.println(removeJSONCharacters(l));
                }
            }

            return null;
        }
    }
}

