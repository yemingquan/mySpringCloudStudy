package com.example.springBootDemo.util;

import com.example.springBootDemo.config.components.enums.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Scanner;

/**
 * @所属模块
 * @描述
 * @创建人 xiaoYe
 * @创建时间 2020/4/11 17:38
 * @备注
 */
@Slf4j
public class FileUtil {

    public static void main(String[] args) throws Exception {
        // 检测参数
        if (args.length != 4) {
            System.out
                    .println("Usage:java FileReplaceKeywords sourceFile targetFile initWord newWord ");
            System.exit(0);
        }
        String sourceFile = args[0];
        String targetFile = args[1];
        String initWord = args[2];
        String newWord = args[3];
        File source = new File(sourceFile);
        File target = new File(targetFile);
        // 检查文件是否存在
        if (!source.exists()) {
            System.out.println("-- Sorry,source file：" + sourceFile
                    + " can't be found. --");
            System.exit(0);
        }

        Scanner input = new Scanner(source);
        PrintWriter writer = new PrintWriter(target);
        while (input.hasNext()) {
            String newStr = input.nextLine().replaceAll(initWord, newWord);
            writer.write(newStr + "\n");
        }
        input.close();
        writer.close();
        System.out.println("--- finish ----");

    }


    /**
     * 根据流的前端来判断文件类型
     * TODO 未测试
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String getSuffByStream(InputStream inputStream) throws IOException {
//      获取流前端10位
        byte[] buffer = new byte[10];
        inputStream.read(buffer);

//       前端10位流 转换为 字符串
        StringBuilder hexFileTypeStr = new StringBuilder();
        for (byte b : buffer) {
            String hexString = Integer.toHexString(b & 0xFF);
            if (hexString.length() < 2) {
                hexFileTypeStr.append("0");
            }
            hexFileTypeStr.append(hexString);
        }
        String hexFileType = hexFileTypeStr.toString();

        // 获取当前文件的真实类型
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (hexFileType.startsWith(fileTypeEnum.getValue())) {
                return fileTypeEnum.toString();
            }
        }
        return null;
    }

    /**
     * 复制文件到目标文件夹
     *
     * @param input
     * @param destAllFileName
     * @return -1：文件传输失败，1：文件正常
     */
    public static int copyFile(InputStream input, String destAllFileName) {
        FileOutputStream out = null;
        try {
            // 写文件
            File destFile = new File(destAllFileName);
            out = new FileOutputStream(destFile);
            copyFile(input, out);
            return 1;
        } catch (Exception ex) {
            log.error("文件复制失败", ex);
            return -1;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                log.error("文件复制失败", e);
                return -1;
            }
        }
    }

    /**
     * 复制缓冲输入流到缓冲输出流
     *
     * @param input
     * @param output
     * @return 1成功
     * @throws IOException
     */
    public static Long copyFile(InputStream input, OutputStream output) throws IOException {
        BufferedOutputStream bufferOutput = null;
        BufferedInputStream bufferInput = null;
        try {
            bufferOutput = new BufferedOutputStream(output);
            bufferInput = new BufferedInputStream(input);

            byte[] buffer = new byte[4096];
            Long size = 0L;
            int length = -1;
            while ((length = bufferInput.read(buffer)) != -1) {
                size += length;
                bufferOutput.write(buffer, 0, length);
                bufferOutput.flush();
            }
            return size;
        } finally {
            if (bufferOutput != null) {
                bufferOutput.close();
            }
            if (bufferInput != null) {
                bufferInput.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 目录创建
     *
     * @param path
     */
    public static void mkdirs(String path) {
        File f = new File(path);
        if (!f.exists()) {
            log.info("创建文件夹:[{}]", path);
            f.mkdirs();
        }
    }

    /**
     * 通过字符串创建文件
     *
     * @param s
     * @param destAllFileName
     * @throws Exception
     */
    public static void createFileByString(String s, String destAllFileName) throws Exception {
        File destFile = new File(destAllFileName);
        log.info("创建文件:[{}]", destAllFileName);
        mkdirs(destFile.getParentFile().getAbsolutePath());

        PrintWriter writer = new PrintWriter(destFile);

        writer.write(s + "\n");
        writer.flush();
        writer.close();
    }

}

//    private MultipartFile getMultipartFile(File file) {
//        FileInputStream fileInputStream = null;
//        MultipartFile multipartFile = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
//                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return multipartFile;
//    }
//
//    private File getFileByMuti(MultipartFile multipartFile) {
//        //文件上传前的名称
//        String fileName = multipartFile.getOriginalFilename();
//        File file = new File(fileName);
//        OutputStream out = null;
//        try {
//            //获取文件流，以文件流的方式输出到新文件
////    InputStream in = multipartFile.getInputStream();
//            out = new FileOutputStream(file);
//            byte[] ss = multipartFile.getBytes();
//            for (int i = 0; i < ss.length; i++) {
//                out.write(ss[i]);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return file;
//    }