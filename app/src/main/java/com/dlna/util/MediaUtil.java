package com.dlna.util;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtil {
    private static final String DIDL_LITE_FOOTER = "</DIDL-Lite>";
    private static final String DIDL_LITE_HEADER = "<?xml version=\"1.0\"?>" + "<DIDL-Lite " + "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" " +
            "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " + "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" " +
            "xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\">";

    public static String pushMediaToRender(String url, String id, String name, String duration) {
        long size = 0;
        long bitrate = 0;
        Res res = new Res(new MimeType(ProtocolInfo.WILDCARD, ProtocolInfo.WILDCARD), size, url);

        String creator = "unknow";
        String resolution = "unknow";
        VideoItem videoItem = new VideoItem(id, "0", name, creator, res);

        String metadata = createItemMetadata(videoItem);
        return metadata;
    }


    public static String createItemMetadata(DIDLObject item) {
        StringBuilder metadata = new StringBuilder();
        metadata.append(DIDL_LITE_HEADER);

        metadata.append(String.format("<item id=\"%s\" parentID=\"%s\" restricted=\"%s\">", item.getId(), item.getParentID(), item.isRestricted() ? "1" : "0"));

        metadata.append(String.format("<dc:title>%s</dc:title>", item.getTitle()));
        String creator = item.getCreator();
        if (creator != null) {
            creator = creator.replaceAll("<", "_");
            creator = creator.replaceAll(">", "_");
        }
        metadata.append(String.format("<upnp:artist>%s</upnp:artist>", creator));

        metadata.append(String.format("<upnp:class>%s</upnp:class>", item.getClazz().getValue()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        String time = sdf.format(now);
        metadata.append(String.format("<dc:date>%s</dc:date>", time));

        // metadata.append(String.format("<upnp:album>%s</upnp:album>",
        // item.get);

        // <res protocolInfo="http-get:*:audio/mpeg:*"
        // resolution="640x478">http://192.168.1.104:8088/Music/07.我醒著做夢.mp3</res>

        Res res = item.getFirstResource();
        if (res != null) {
            // protocol info
            String protocolinfo = "";
            ProtocolInfo pi = res.getProtocolInfo();
            if (pi != null) {
                protocolinfo = String.format("protocolInfo=\"%s:%s:%s:%s\"", pi.getProtocol(), pi.getNetwork(), pi.getContentFormatMimeType(), pi
                        .getAdditionalInfo());
            }

            // resolution, extra info, not adding yet
            String resolution = "";
            if (res.getResolution() != null && res.getResolution().length() > 0) {
                resolution = String.format("resolution=\"%s\"", res.getResolution());
            }

            // duration
            String duration = "";
            if (res.getDuration() != null && res.getDuration().length() > 0) {
                duration = String.format("duration=\"%s\"", res.getDuration());
            }

            // res begin
            //            metadata.append(String.format("<res %s>", protocolinfo)); // no resolution & duration yet
            metadata.append(String.format("<res %s %s %s>", protocolinfo, resolution, duration));

            // url
            String url = res.getValue();
            metadata.append(url);

            // res end
            metadata.append("</res>");
        }
        metadata.append("</item>");

        metadata.append(DIDL_LITE_FOOTER);

        return metadata.toString();
    }
}
