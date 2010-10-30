package com.mojolly.scapulet

import akka.util.Logging
import java.security.{NoSuchAlgorithmException, MessageDigest}
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import org.apache.commons.codec.binary.Hex

object StringUtil extends Logging {
  val UTF_8 = "UTF-8"
  val Utf8 = Charset.forName(UTF_8)

  private lazy val digest = try {
    MessageDigest.getInstance("SHA-1")
  } catch {
    case e: NoSuchAlgorithmException => {
      log error "Failed to load the SHA-1 algorithm. Scapulet will be unable to function properly."
      exit(1) // Fatal error bail
    }
    case e => throw e
  }

  def hash(data: String) = try {
    digest.update(data.getBytes(UTF_8))
    hexEncode(digest.digest)
    //Hex.encodeHexString(digest.digest)
  } catch {
    case e: UnsupportedEncodingException => {
      log error "Failed to use the UTF-8 encoding. Scapulet will be unable to function properly."
      exit(1)
    }
    case e => throw e
  }

  def hexEncode(bytes: Array[Byte]) =  ((new StringBuilder(bytes.length * 2) /: bytes) { (sb, b) =>
    if((b.toInt & 0xff) < 0x10) sb.append("0")
    sb.append(Integer.toString(b.toInt & 0xff, 16))
  }).toString

  

}