package com.group.quasi.util


import java.security.{PrivateKey, PublicKey}
import java.util.Base64

object AsymKeys{
  def fromAlgo(asymAlgo: String): AsymKeys = asymAlgo match {
    case "SHA256withRSA" => RSASign
    case "SHA384withRSA" => RSASign
    case "SHA512withRSA" => RSASign
    case "SHA256withECDSA" => DSASign
    case "SHA384withECDSA" => DSASign
    case "SHA512withECDSA" => DSASign
    case "Ed25519" => Ed25519Sign
    case _ => throw new RuntimeException("unsupported asym algo")
  }
}


trait AsymKeys {

  def publicKeyFromBytes(bytes: Array[Byte]): PublicKey
  def privateKeyFromBytes(bytes: Array[Byte]): PrivateKey

  def publicKeyFromBase64String(base64String: String): PublicKey = publicKeyFromBytes(bytesFromString(base64String))

  def privateKeyFromBase64String(base64String: String): PrivateKey = privateKeyFromBytes(bytesFromString(base64String))

  def bytesToString(bytes: Array[Byte]): String = Base64.getEncoder.encodeToString(bytes)

  def bytesFromString(base64String: String): Array[Byte] = Base64.getDecoder.decode(base64String)

}
