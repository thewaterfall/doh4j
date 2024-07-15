package org.waterfallio.doh4j.specification;

/**
 * Constants for DNS record types.
 *
 * @see <a href="https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml">Domain Name System (DNS)
 * Parameters</a>
 */
public class Type {
  /**
   * Address
   */
  public static final int A = 1;

  /**
   * Name server
   */
  public static final int NS = 2;

  /**
   * Mail destination
   */
  public static final int MD = 3;

  /**
   * Mail forwarder
   */
  public static final int MF = 4;

  /**
   * Canonical name (alias)
   */
  public static final int CNAME = 5;

  /**
   * Start of authority
   */
  public static final int SOA = 6;

  /**
   * Mailbox domain name
   */
  public static final int MB = 7;

  /**
   * Mail group member
   */
  public static final int MG = 8;

  /**
   * Mail rename name
   */
  public static final int MR = 9;

  /**
   * Null record
   */
  public static final int NULL = 10;

  /**
   * Well known services
   */
  public static final int WKS = 11;

  /**
   * Domain name pointer
   */
  public static final int PTR = 12;

  /**
   * Host information
   */
  public static final int HINFO = 13;

  /**
   * Mailbox information
   */
  public static final int MINFO = 14;

  /**
   * Mail routing information
   */
  public static final int MX = 15;

  /**
   * Text strings
   */
  public static final int TXT = 16;

  /**
   * Responsible person
   */
  public static final int RP = 17;

  /**
   * AFS cell database
   */
  public static final int AFSDB = 18;

  /**
   * X.25 calling address
   */
  public static final int X25 = 19;

  /**
   * ISDN calling address
   */
  public static final int ISDN = 20;

  /**
   * Router
   */
  public static final int RT = 21;

  /**
   * NSAP address
   */
  public static final int NSAP = 22;

  /**
   * Reverse NSAP address
   */
  public static final int NSAP_PTR = 23;

  /**
   * Signature
   */
  public static final int SIG = 24;

  /**
   * Key
   */
  public static final int KEY = 25;

  /**
   * X.400 mail mapping
   */
  public static final int PX = 26;

  /**
   * Geographical position
   */
  public static final int GPOS = 27;

  /**
   * IPv6 address
   */
  public static final int AAAA = 28;

  /**
   * Location
   */
  public static final int LOC = 29;

  /**
   * Next valid name in zone
   */
  public static final int NXT = 30;

  /**
   * Endpoint identifier
   */
  public static final int EID = 31;

  /**
   * Nimrod locator
   */
  public static final int NIMLOC = 32;

  /**
   * Server selection
   */
  public static final int SRV = 33;

  /**
   * ATM address
   */
  public static final int ATMA = 34;

  /**
   * Naming authority pointer
   */
  public static final int NAPTR = 35;

  /**
   * Key exchange
   */
  public static final int KX = 36;

  /**
   * Certificate
   */
  public static final int CERT = 37;

  /**
   * IPv6 address (historic)
   */
  public static final int A6 = 38;

  /**
   * Non-terminal name redirection
   */
  public static final int DNAME = 39;

  /**
   * Kitchen Sink (April Fools' Day RR)
   */
  public static final int SINK = 40;

  /**
   * Options - contains EDNS metadata
   */
  public static final int OPT = 41;

  /**
   * Address Prefix List
   */
  public static final int APL = 42;

  /**
   * Delegation Signer
   */
  public static final int DS = 43;

  /**
   * SSH Key Fingerprint
   */
  public static final int SSHFP = 44;

  /**
   * IPSEC key
   */
  public static final int IPSECKEY = 45;

  /**
   * Resource Record Signature
   */
  public static final int RRSIG = 46;

  /**
   * Next Secure Name
   */
  public static final int NSEC = 47;

  /**
   * DNSSEC Key
   */
  public static final int DNSKEY = 48;

  /**
   * Dynamic Host Configuration Protocol (DHCP) ID
   */
  public static final int DHCID = 49;

  /**
   * Next SECure, 3rd edition
   */
  public static final int NSEC3 = 50;

  /**
   * Next SECure PARAMeter
   */
  public static final int NSEC3PARAM = 51;

  /**
   * Transport Layer Security Authentication
   */
  public static final int TLSA = 52;

  /**
   * S/MIME cert association
   */
  public static final int SMIMEA = 53;

  /**
   * Host Identity Protocol (HIP)
   */
  public static final int HIP = 55;

  /**
   * Zone Status (ZS).
   */
  public static final int NINFO = 56;

  /**
   * RKEY DNS Resource Record, used for encryption of NAPTR records.
   */
  public static final int RKEY = 57;

  /**
   * DNSSEC Trust Anchor History Service.
   */
  public static final int TALINK = 58;

  /**
   * Child Delegation Signer
   */
  public static final int CDS = 59;

  /**
   * Child DNSKEY
   */
  public static final int CDNSKEY = 60;

  /**
   * OpenPGP Key
   */
  public static final int OPENPGPKEY = 61;

  /**
   * Child-to-Parent Synchronization.
   */
  public static final int CSYNC = 62;

  /**
   * Message Digest for DNS Zones.
   */
  public static final int ZONEMD = 63;

  /**
   * Service Location and Parameter Binding
   */
  public static final int SVCB = 64;

  /**
   * HTTPS Service Location and Parameter Binding
   */
  public static final int HTTPS = 65;

  /**
   * Sender Policy Framework
   */
  public static final int SPF = 99;

  /**
   * IANA-Reserved
   */
  public static final int UINFO = 100;

  /**
   * IANA-Reserved
   */
  public static final int UID = 101;

  /**
   * IANA-Reserved
   */
  public static final int GID = 102;

  /**
   * IANA-Reserved
   */
  public static final int UNSPEC = 103;

  /**
   * Node Identifier (NID).
   */
  public static final int NID = 104;

  /**
   * 32-bit Locator value for ILNPv4-capable node.
   */
  public static final int L32 = 105;

  /**
   * Unsigned 64-bit Locator value for ILNPv6-capable node.
   */
  public static final int L64 = 106;

  /**
   * Name of a subnetwork for ILNP.
   */
  public static final int LP = 107;

  /**
   * EUI-48 Address.
   */
  public static final int EUI48 = 108;

  /**
   * EUI-64 Address.
   */
  public static final int EUI64 = 109;

  /**
   * Transaction key
   */
  public static final int TKEY = 249;

  /**
   * Transaction signature
   */
  public static final int TSIG = 250;

  /**
   * Incremental zone transfer
   */
  public static final int IXFR = 251;

  /**
   * Zone transfer
   */
  public static final int AXFR = 252;

  /**
   * Transfer mailbox records
   */
  public static final int MAILB = 253;

  /**
   * Mail agent RRs (obsolete)
   */
  public static final int MAILA = 254;

  /**
   * Matches any type
   */
  public static final int ANY = 255;

  /**
   * URI
   */
  public static final int URI = 256;

  /**
   * Certification Authority Authorization
   */
  public static final int CAA = 257;

  /**
   * Application Visibility and Control
   */
  public static final int AVC = 258;

  /**
   * Digital Object Architecture
   */
  public static final int DOA = 259;

  /**
   * Automatic Multicast Tunneling Relay
   */
  public static final int AMTRELAY = 260;

  /**
   * DNSSEC Trust Authorities
   */
  public static final int TA = 32768;

  /**
   * DNSSEC Lookaside Validation
   */
  public static final int DLV = 32769;
}
