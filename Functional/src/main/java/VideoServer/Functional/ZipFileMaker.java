package VideoServer.Functional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileMaker {
    private ZipFileMaker(){}
    public static void Make(File output,File inputDir) throws IOException {
        if(output.exists())return;
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // 遍历要压缩的文件
        for (File file : inputDir.listFiles()) {
            FileInputStream input = new FileInputStream(file);

            // 为每个文件创建一个 ZIP 条目
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            // 写入文件内容到 ZIP 文件
            zos.write(input.readAllBytes());

            // 关闭当前 ZIP 条目
            zos.closeEntry();
            input.close();
        }

        // 关闭 ZIP 输出流
        zos.close();
        fos.close();
    }
}
