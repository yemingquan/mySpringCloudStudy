package com.example.springBootDemo.config.components.enums;

/**
 * @所属模块 配置-枚举-文件格式枚举
 * @描述 <p>用于校验文件头部信息，判断文件类型<p/>
 * @创建人 xiaoYe
 * @创建时间
 * @备注
 */
public enum FileTypeEnum {
	JPG("ffd8ffe000104a464946"),
    PNG("89504e470d0a1a0a0000"),
    GIF("47494638396126026f01"),
    TIF("49492a00227105008037"),
    BMP_1("424d228c010000000000"),//16色位图(bmp)
    BMP_2("424d8240090000000000"),//24位位图(bmp)
    BMP_3("424d8e1b030000000000"),//256色位图(bmp)
    DWG("41433130313500000000"),
    HTML("3c21444f435459504520"),
    HTM("3c21646f637479706520"),
    CSS("48544d4c207b0d0a0942"),
    JS("696b2e71623d696b2e71"),
    RTF("7b5c727466315c616e73"),
    PSD("38425053000100000000"),
    EML("46726f6d3a203d3f6762"),
    DOC("d0cf11e0a1b11ae10000"),
    VSD("d0cf11e0a1b11ae10000"),
    MDB("5374616E64617264204A"),
    PS("252150532D41646F6265"),
    PDF("255044462d312e350d0a"),
    RMVB("2e524d46000000120001"),
    RM("2e524d46000000120001"),
    FLV("464c5601050000000900"),
    F4V("464c5601050000000900"),
    MP4("00000020667479706d70"),
    MP3("49443303000000002176"),
    MPG("000001ba210001000180"),
    WMV("3026b2758e66cf11a6d9"),
    ASF("3026b2758e66cf11a6d9"),
    WAV("52494646e27807005741"),
    AVI("52494646d07d60074156"),
    MID("4d546864000000060001"),
    ZIP("504b0304140000000800"),
    RAR("526172211a0700cf9073"),
    INI("235468697320636f6e66"),
    JAR("504b03040a0000000000"),
    EXE("4d5a9000030000000400"),
    JSP("3c25402070616765206c"),
    MF("4d616e69666573742d56"),
    XML("3c3f786d6c2076657273"),
    SQL("494e5345525420494e54"),
    JAVA("7061636b616765207765"),
    BAT("406563686f206f66660d"),
    GZ("1f8b0800000000000000"),
    PROPERTIES("6c6f67346a2e726f6f74"),
    CLASS("cafebabe0000002e0041"),
    CHM("49545346030000006000"),
    MXP("04000000010000001300"),
    DOCX("504b0304140006000800"),
    WPS("d0cf11e0a1b11ae10000"),
    TORRENT("6431303a637265617465"),
    MOV("6D6F6F76"),
    WPD("FF575043"),
    DBX("CFAD12FEC5FD746F"),
    PST("2142444E"),
    QDF("AC9EBD8F"),
    PWL("E3828596"),
    RAM("2E7261FD");

    private String value = "";

    private FileTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
