package org.mds.video.hls.model.tags;


import org.mds.video.hls.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Media segments MAY be encrypted.  The EXT-X-KEY tag specifies how to
 * decrypt them.  It applies to every media URI that appears between it
 * and the next EXT-X-KEY tag in the Playlist file (if any).  Its format
 * is:
 * <p/>
 * #EXT-X-KEY:<attribute-list>
 * <p/>
 * The following attributes are defined:
 * (1) The METHOD attribute specifies the encryption method.  It is of type
 * enumerated-string.  Each EXT-X-KEY tag MUST contain a METHOD
 * attribute.
 * Two methods are defined: NONE and AES-128.
 * An encryption method of NONE means that media segments are not
 * encrypted.  If the encryption method is NONE, the URI and the IV
 * attributes MUST NOT be present.
 * An encryption method of AES-128 means that media segments are
 * encrypted using the Advanced Encryption Standard [AES_128] with a
 * 128-bit key and PKCS7 padding [RFC5652].  If the encryption method is
 * AES-128, the URI attribute MUST be present.  The IV attribute MAY be
 * present.
 * (2) (if the encryption method isAES-128, the URI attribute MUST be present)
 * The URI attribute specifies how to obtain the key.  Its value is a quoted-string
 * that contains a URI [RFC3986] for the key.
 * (3) (if the encryption method is AES-128, the IV attribute MAY be present)
 * The IV attribute, if present, specifies the Initialization Vector to
 * be used with the key.  Its value is a hexadecimal-integer.  The IV
 * attribute appeared in protocol version 2.
 * <p/>
 * If the Playlist file does not contain an EXT-X-KEY tag then media
 * segments are not encrypted.
 *
 * @author Randall.mo
 */
public class ExtKeyTag extends M3uTag {
    //Here all constants are not final.
    //In application runtime, these values may be injected.
    public static String EXTKEY_TAG = "#EXT-X-KEY";
    public static String ATTR_METHOD = "METHOD";
    public static String ATTR_URI = "URI";
    public static String ATTR_IV = "IV";
    public static String METHOD_OPTION_NONE = "NONE";
    public static String METHOD_OPTION_AES128 = "AES-128";

    private String method, uri, iv;

    public static AttributeHandler<ExtKeyTag> attributeHandler = new AttributeHandler<ExtKeyTag>() {
        @Override
        public void findAttribute(ExtKeyTag tag, String attributeName, String value) {
            if (ATTR_METHOD.equals(attributeName)) {
                tag.method = value;
                return;
            }
            if (ATTR_URI.equals(attributeName)) {
                tag.uri = value;
                if (tag.uri != null)
                    tag.uri = StringUtils.remove(tag.uri, C_DQUOTE);
                return;
            }
            if (ATTR_IV.equals(attributeName)) {
                tag.iv = value;
                return;
            }
        }
    };

    private static Builder builder = new Builder() {
        @Override
        public M3uTag build(String line) {
            return forLine(line);
        }
    };

    public static Builder builder() {
        return builder;
    }

    public static ExtKeyTag forLine(String line) {
        if (!isLineFor(line, EXTKEY_TAG)) return null;

        ExtKeyTag tag = new ExtKeyTag();
        scanAttributes(tag, line, attributeHandler);
        return tag;
    }

    public String getMethod() {
        return method;
    }

    public ExtKeyTag setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public ExtKeyTag setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getIv() {
        return iv;
    }

    public ExtKeyTag setIv(String iv) {
        this.iv = iv;
        return this;
    }

    @Override
    public void emit(StringBuilder inputOutput) {
        inputOutput.append(EXTKEY_TAG).append(C_COLON)
                .append(ATTR_METHOD).append(C_EQUAL).append(this.method);
        if (METHOD_OPTION_AES128.equals(this.method)) {
            inputOutput.append(C_COMMA).append(ATTR_URI).append(C_EQUAL)
                    .append(C_DQUOTE).append(this.uri).append(C_DQUOTE);
            if (!StringUtils.isEmpty(this.iv)) {
                inputOutput.append(C_COMMA).append(ATTR_IV).append(C_EQUAL).append(this.iv);
            }
        }
        inputOutput.append(S_CRLF);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ExtKeyTag)) {
            return false;
        }
        ExtKeyTag inputObj = (ExtKeyTag) obj;
        //generally, if the uri is not null, compare the uri is OK.
        if (this.uri != null) {
            return this.uri.equals(inputObj.uri);
        }

        return true;
    }
}
