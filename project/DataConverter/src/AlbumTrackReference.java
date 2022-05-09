public class AlbumTrackReference {
    String idFrom, idTo;
    Integer trackNumber;

    public AlbumTrackReference(String idFrom, String idTo, Integer trackNumber) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.trackNumber = trackNumber;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }
}
