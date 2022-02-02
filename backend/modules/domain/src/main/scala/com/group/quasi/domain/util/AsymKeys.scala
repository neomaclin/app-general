package com.group.quasi.domain.util

import java.nio.file.{Files, Paths}
import java.security.{PrivateKey, PublicKey}
import java.util.Base64

trait AsymKeys {
  val KEY_FOLDER = "./key/"
  def publicKeyFromBytes(bytes: Array[Byte]): PublicKey
  def privateKeyFromBytes(bytes: Array[Byte]): PrivateKey

  // Key import and conversion utilities

  def getBytesFromPKCS_8_DER_File(filename: String): Array[Byte] = Files.readAllBytes(Paths.get(KEY_FOLDER + filename))

  // public key from a PKCS#8 .der file
  def publicKeyFromFile(filename: String): PublicKey = publicKeyFromBytes(getBytesFromPKCS_8_DER_File(filename))

  def privateKeyFromFile(filename: String): PrivateKey = privateKeyFromBytes(getBytesFromPKCS_8_DER_File(filename))

  // convert a Base64 String (of PKCS#8 bytes) to a PublicKey
  def publicKeyFromBase64String(base64String: String): PublicKey = publicKeyFromBytes(bytesFromString(base64String))

  def privateKeyFromBase64String(base64String: String): PrivateKey = privateKeyFromBytes(bytesFromString(base64String))
  // Convert a PrivateKey to a Base64 printable String
  def privateKeyAsString(privateKey: PrivateKey): String = bytesToString(privateKey.getEncoded)
  // Convert a PublicKey to a Base64 printable String
  def publicKeyAsString(publicKey: PublicKey): String = bytesToString(publicKey.getEncoded)
  // Convert binary byte array to a Base64 printable string
  def bytesToString(bytes: Array[Byte]): String = Base64.getEncoder.encodeToString(bytes)

  def bytesFromString(base64String: String): Array[Byte] = Base64.getDecoder.decode(base64String)

}
