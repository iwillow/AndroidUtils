package com.iwillow.app.android.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by https://github.com/iwillow/ on 2016/11/28.
 */

public class MimeUtils {
    public static final String MIME_JSON = "application/json";

    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(".*", "application/octet-stream");
        map.put(".tif", "image/tiff");
        map.put(".001", "application/x-001");
        map.put(".301", "application/x-301");
        map.put(".323", "text/h323");
        map.put(".906", "application/x-906");
        map.put(".907", "drawing/907");
        map.put(".a11", "application/x-a11");
        map.put(".acp", "audio/x-mei-aac");
        map.put(".ai", "application/postscript");
        map.put(".aif", "audio/aiff");
        map.put(".aifc", "audio/aiff");
        map.put(".aiff", "audio/aiff");
        map.put(".anv", "application/x-anv");
        map.put(".asa", "text/asa");
        map.put(".asf", "video/x-ms-asf");
        map.put(".asp", "text/asp");
        map.put(".asx", "video/x-ms-asf");
        map.put(".au", "audio/basic");
        map.put(".avi", "video/avi");
        map.put(".awf", "application/vnd.adobe.workflow");
        map.put(".biz", "text/xml");
        map.put(".bmp", "application/x-bmp");
        map.put(".bot", "application/x-bot");
        map.put(".c4t", "application/x-c4t");
        map.put(".c90", "application/x-c90");
        map.put(".cal", "application/x-cals");
        map.put(".cat", "application/vnd.ms-pki.seccat");
        map.put(".cdf", "application/x-netcdf");
        map.put(".cdr", "application/x-cdr");
        map.put(".cel", "application/x-cel");
        map.put(".cer", "application/x-x509-ca-cert");
        map.put(".cg4", "application/x-g4");
        map.put(".cgm", "application/x-cgm");
        map.put(".cit", "application/x-cit");
        map.put(".class", "java/*");
        map.put(".cml", "text/xml");
        map.put(".cmp", "application/x-cmp");
        map.put(".cmx", "application/x-cmx");
        map.put(".cot", "application/x-cot");
        map.put(".crl", "application/pkix-crl");
        map.put(".crt", "application/x-x509-ca-cert");
        map.put(".csi", "application/x-csi");
        map.put(".css", "text/css");
        map.put(".cut", "application/x-cut");
        map.put(".dbf", "application/x-dbf");
        map.put(".dbm", "application/x-dbm");
        map.put(".dbx", "application/x-dbx");
        map.put(".dcd", "text/xml");
        map.put(".dcx", "application/x-dcx");
        map.put(".der", "application/x-x509-ca-cert");
        map.put(".dgn", "application/x-dgn");
        map.put(".dib", "application/x-dib");
        map.put(".dll", "application/x-msdownload");
        map.put(".doc", "application/msword");
        map.put(".dot", "application/msword");
        map.put(".drw", "application/x-drw");
        map.put(".dtd", "text/xml");
        map.put(".dwf", "Model/vnd.dwf");
        map.put(".dwf", "application/x-dwf");
        map.put(".dwg", "application/x-dwg");
        map.put(".dxb", "application/x-dxb");
        map.put(".dxf", "application/x-dxf");
        map.put(".edn", "application/vnd.adobe.edn");
        map.put(".emf", "application/x-emf");
        map.put(".eml", "message/rfc822");
        map.put(".ent", "text/xml");
        map.put(".epi", "application/x-epi");
        map.put(".eps", "application/x-ps");
        map.put(".eps", "application/postscript");
        map.put(".etd", "application/x-ebx");
        map.put(".exe", "application/x-msdownload");
        map.put(".fax", "image/fax");
        map.put(".fdf", "application/vnd.fdf");
        map.put(".fif", "application/fractals");
        map.put(".fo", "text/xml");
        map.put(".frm", "application/x-frm");
        map.put(".g4", "application/x-g4");
        map.put(".gbr", "application/x-gbr");
        map.put(".", "application/x-");
        map.put(".gif", "image/gif");
        map.put(".gl2", "application/x-gl2");
        map.put(".gp4", "application/x-gp4");
        map.put(".hgl", "application/x-hgl");
        map.put(".hmr", "application/x-hmr");
        map.put(".hpg", "application/x-hpgl");
        map.put(".hpl", "application/x-hpl");
        map.put(".hqx", "application/mac-binhex40");
        map.put(".hrf", "application/x-hrf");
        map.put(".hta", "application/hta");
        map.put(".htc", "text/x-component");
        map.put(".htm", "text/html");
        map.put(".html", "text/html");
        map.put(".htt", "text/webviewhtml");
        map.put(".htx", "text/html");
        map.put(".icb", "application/x-icb");
        map.put(".ico", "image/x-icon");
        map.put(".ico", "application/x-ico");
        map.put(".iff", "application/x-iff");
        map.put(".ig4", "application/x-g4");
        map.put(".igs", "application/x-igs");
        map.put(".iii", "application/x-iphone");
        map.put(".img", "application/x-img");
        map.put(".ins", "application/x-internet-signup");
        map.put(".isp", "application/x-internet-signup");
        map.put(".IVF", "video/x-ivf");
        map.put(".java", "java/*");
        map.put(".jfif", "image/jpeg");
        map.put(".jpe", "image/jpeg");
        map.put(".jpe", "application/x-jpe");
        map.put(".jpeg", "image/jpeg");
        map.put(".jpg", "image/jpeg");
        map.put(".jpg", "application/x-jpg");
        map.put(".js", "application/x-javascript");
        map.put(".json", "application/json");
        map.put(".jsp", "text/html");
        map.put(".la1", "audio/x-liquid-file");
        map.put(".lar", "application/x-laplayer-reg");
        map.put(".latex", "application/x-latex");
        map.put(".lavs", "audio/x-liquid-secure");
        map.put(".lbm", "application/x-lbm");
        map.put(".lmsff", "audio/x-la-lms");
        map.put(".ls", "application/x-javascript");
        map.put(".ltr", "application/x-ltr");
        map.put(".m1v", "video/x-mpeg");
        map.put(".m2v", "video/x-mpeg");
        map.put(".m3u", "audio/mpegurl");
        map.put(".m4e", "video/mpeg4");
        map.put(".mac", "application/x-mac");
        map.put(".man", "application/x-troff-man");
        map.put(".math", "text/xml");
        map.put(".mdb", "application/msaccess");
        map.put(".mdb", "application/x-mdb");
        map.put(".mfp", "application/x-shockwave-flash");
        map.put(".mht", "message/rfc822");
        map.put(".mhtml", "message/rfc822");
        map.put(".mi", "application/x-mi");
        map.put(".mid", "audio/mid");
        map.put(".midi", "audio/mid");
        map.put(".mil", "application/x-mil");
        map.put(".mml", "text/xml");
        map.put(".mnd", "audio/x-musicnet-download");
        map.put(".mns", "audio/x-musicnet-stream");
        map.put(".mocha", "application/x-javascript");
        map.put(".movie", "video/x-sgi-movie");
        map.put(".mp1", "audio/mp1");
        map.put(".mp2", "audio/mp2");
        map.put(".mp2v", "video/mpeg");
        map.put(".mp3", "audio/mp3");
        map.put(".mp4", "video/mpeg4");
        map.put(".mpa", "video/x-mpg");
        map.put(".mpd", "application/vnd.ms-project");
        map.put(".mpe", "video/x-mpeg");
        map.put(".mpeg", "video/mpg");
        map.put(".mpg", "video/mpg");
        map.put(".mpga", "audio/rn-mpeg");
        map.put(".mpp", "application/vnd.ms-project");
        map.put(".mps", "video/x-mpeg");
        map.put(".mpt", "application/vnd.ms-project");
        map.put(".mpv", "video/mpg");
        map.put(".mpv2", "video/mpeg");
        map.put(".mpw", "application/vnd.ms-project");
        map.put(".mpx", "application/vnd.ms-project");
        map.put(".mtx", "text/xml");
        map.put(".mxp", "application/x-mmxp");
        map.put(".net", "image/pnetvue");
        map.put(".nrf", "application/x-nrf");
        map.put(".nws", "message/rfc822");
        map.put(".odc", "text/x-ms-odc");
        map.put(".out", "application/x-out");
        map.put(".p10", "application/pkcs10");
        map.put(".p12", "application/x-pkcs12");
        map.put(".p7b", "application/x-pkcs7-certificates");
        map.put(".p7c", "application/pkcs7-mime");
        map.put(".p7m", "application/pkcs7-mime");
        map.put(".p7r", "application/x-pkcs7-certreqresp");
        map.put(".p7s", "application/pkcs7-signature");
        map.put(".pc5", "application/x-pc5");
        map.put(".pci", "application/x-pci");
        map.put(".pcl", "application/x-pcl");
        map.put(".pcx", "application/x-pcx");
        map.put(".pdf", "application/pdf");
        map.put(".pdx", "application/vnd.adobe.pdx");
        map.put(".pfx", "application/x-pkcs12");
        map.put(".pgl", "application/x-pgl");
        map.put(".pic", "application/x-pic");
        map.put(".pko", "application/vnd.ms-pki.pko");
        map.put(".pl", "application/x-perl");
        map.put(".plg", "text/html");
        map.put(".pls", "audio/scpls");
        map.put(".plt", "application/x-plt");
        map.put(".png", "image/png");
        map.put(".png", "application/x-png");
        map.put(".pot", "application/vnd.ms-powerpoint");
        map.put(".ppa", "application/vnd.ms-powerpoint");
        map.put(".ppm", "application/x-ppm");
        map.put(".pps", "application/vnd.ms-powerpoint");
        map.put(".ppt", "application/vnd.ms-powerpoint");
        map.put(".ppt", "application/x-ppt");
        map.put(".pr", "application/x-pr");
        map.put(".prf", "application/pics-rules");
        map.put(".prn", "application/x-prn");
        map.put(".prt", "application/x-prt");
        map.put(".ps", "application/x-ps");
        map.put(".ps", "application/postscript");
        map.put(".ptn", "application/x-ptn");
        map.put(".pwz", "application/vnd.ms-powerpoint");
        map.put(".r3t", "text/vnd.rn-realtext3d");
        map.put(".ra", "audio/vnd.rn-realaudio");
        map.put(".ram", "audio/x-pn-realaudio");
        map.put(".ras", "application/x-ras");
        map.put(".rat", "application/rat-file");
        map.put(".rdf", "text/xml");
        map.put(".rec", "application/vnd.rn-recording");
        map.put(".red", "application/x-red");
        map.put(".rgb", "application/x-rgb");
        map.put(".rjs", "application/vnd.rn-realsystem-rjs");
        map.put(".rjt", "application/vnd.rn-realsystem-rjt");
        map.put(".rlc", "application/x-rlc");
        map.put(".rle", "application/x-rle");
        map.put(".rm", "application/vnd.rn-realmedia");
        map.put(".rmf", "application/vnd.adobe.rmf");
        map.put(".rmi", "audio/mid");
        map.put(".rmj", "application/vnd.rn-realsystem-rmj");
        map.put(".rmm", "audio/x-pn-realaudio");
        map.put(".rmp", "application/vnd.rn-rn_music_package");
        map.put(".rms", "application/vnd.rn-realmedia-secure");
        map.put(".rmvb", "application/vnd.rn-realmedia-vbr");
        map.put(".rmx", "application/vnd.rn-realsystem-rmx");
        map.put(".rnx", "application/vnd.rn-realplayer");
        map.put(".rp", "image/vnd.rn-realpix");
        map.put(".rpm", "audio/x-pn-realaudio-plugin");
        map.put(".rsml", "application/vnd.rn-rsml");
        map.put(".rt", "text/vnd.rn-realtext");
        map.put(".rtf", "application/msword");
        map.put(".rtf", "application/x-rtf");
        map.put(".rv", "video/vnd.rn-realvideo");
        map.put(".sam", "application/x-sam");
        map.put(".sat", "application/x-sat");
        map.put(".sdp", "application/sdp");
        map.put(".sdw", "application/x-sdw");
        map.put(".sit", "application/x-stuffit");
        map.put(".slb", "application/x-slb");
        map.put(".sld", "application/x-sld");
        map.put(".slk", "drawing/x-slk");
        map.put(".smi", "application/smil");
        map.put(".smil", "application/smil");
        map.put(".smk", "application/x-smk");
        map.put(".snd", "audio/basic");
        map.put(".sol", "text/plain");
        map.put(".sor", "text/plain");
        map.put(".spc", "application/x-pkcs7-certificates");
        map.put(".spl", "application/futuresplash");
        map.put(".spp", "text/xml");
        map.put(".ssm", "application/streamingmedia");
        map.put(".sst", "application/vnd.ms-pki.certstore");
        map.put(".stl", "application/vnd.ms-pki.stl");
        map.put(".stm", "text/html");
        map.put(".sty", "application/x-sty");
        map.put(".svg", "text/xml");
        map.put(".swf", "application/x-shockwave-flash");
        map.put(".tdf", "application/x-tdf");
        map.put(".tg4", "application/x-tg4");
        map.put(".tga", "application/x-tga");
        map.put(".tif", "image/tiff");
        map.put(".tif", "application/x-tif");
        map.put(".tiff", "image/tiff");
        map.put(".tld", "text/xml");
        map.put(".top", "drawing/x-top");
        map.put(".torrent", "application/x-bittorrent");
        map.put(".tsd", "text/xml");
        map.put(".txt", "text/plain");
        map.put(".uin", "application/x-icq");
        map.put(".uls", "text/iuls");
        map.put(".vcf", "text/x-vcard");
        map.put(".vda", "application/x-vda");
        map.put(".vdx", "application/vnd.visio");
        map.put(".vml", "text/xml");
        map.put(".vpg", "application/x-vpeg005");
        map.put(".vsd", "application/vnd.visio");
        map.put(".vsd", "application/x-vsd");
        map.put(".vss", "application/vnd.visio");
        map.put(".vst", "application/vnd.visio");
        map.put(".vst", "application/x-vst");
        map.put(".vsw", "application/vnd.visio");
        map.put(".vsx", "application/vnd.visio");
        map.put(".vtx", "application/vnd.visio");
        map.put(".vxml", "text/xml");
        map.put(".wav", "audio/wav");
        map.put(".wax", "audio/x-ms-wax");
        map.put(".wb1", "application/x-wb1");
        map.put(".wb2", "application/x-wb2");
        map.put(".wb3", "application/x-wb3");
        map.put(".wbmp", "image/vnd.wap.wbmp");
        map.put(".wiz", "application/msword");
        map.put(".wk3", "application/x-wk3");
        map.put(".wk4", "application/x-wk4");
        map.put(".wkq", "application/x-wkq");
        map.put(".wks", "application/x-wks");
        map.put(".wm", "video/x-ms-wm");
        map.put(".wma", "audio/x-ms-wma");
        map.put(".wmd", "application/x-ms-wmd");
        map.put(".wmf", "application/x-wmf");
        map.put(".wml", "text/vnd.wap.wml");
        map.put(".wmv", "video/x-ms-wmv");
        map.put(".wmx", "video/x-ms-wmx");
        map.put(".wmz", "application/x-ms-wmz");
        map.put(".wp6", "application/x-wp6");
        map.put(".wpd", "application/x-wpd");
        map.put(".wpg", "application/x-wpg");
        map.put(".wpl", "application/vnd.ms-wpl");
        map.put(".wq1", "application/x-wq1");
        map.put(".wr1", "application/x-wr1");
        map.put(".wri", "application/x-wri");
        map.put(".wrk", "application/x-wrk");
        map.put(".ws", "application/x-ws");
        map.put(".ws2", "application/x-ws");
        map.put(".wsc", "text/scriptlet");
        map.put(".wsdl", "text/xml");
        map.put(".wvx", "video/x-ms-wvx");
        map.put(".xdp", "application/vnd.adobe.xdp");
        map.put(".xdr", "text/xml");
        map.put(".xfd", "application/vnd.adobe.xfd");
        map.put(".xfdf", "application/vnd.adobe.xfdf");
        map.put(".xhtml", "text/html");
        map.put(".xls", "application/vnd.ms-excel");
        map.put(".xls", "application/x-xls");
        map.put(".xlw", "application/x-xlw");
        map.put(".xml", "text/xml");
        map.put(".xpl", "audio/scpls");
        map.put(".xq", "text/xml");
        map.put(".xql", "text/xml");
        map.put(".xquery", "text/xml");
        map.put(".xsd", "text/xml");
        map.put(".xsl", "text/xml");
        map.put(".xslt", "text/xml");
        map.put(".xwd", "application/x-xwd");
        map.put(".x_b", "application/x-x_b");
        map.put(".sis", "application/vnd.symbian.install");
        map.put(".sisx", "application/vnd.symbian.install");
        map.put(".x_t", "application/x-x_t");
        map.put(".ipa", "application/vnd.iphone");
        map.put(".apk", "application/vnd.android.package-archive");
        map.put(".xap", "application/x-silverlight-app");
    }


    public static String getMimeType(String suffix) {
        if (!TextUtils.isEmpty(suffix) && map.containsKey(suffix.toLowerCase())) {
            return map.get(suffix.toLowerCase());
        }

        return "application/octet-stream";
    }
}
