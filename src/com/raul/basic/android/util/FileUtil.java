package com.raul.basic.android.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * File通用操作工具类
 * @author xiaomiaomiao
 *
 */
public final class FileUtil
{
    /**
     * 附件类型 PIC
     */
    public static final String ATTACH_TYPE_PIC = "picture";
    
    /**
     * 附件类型 PDF
     */
    public static final String ATTACH_TYPE_PDF = "pdf";
    
    /**
     * 附件类型 VIDEO
     */
    public static final String ATTACH_TYPE_VIDEO = "video";
    
    /**
     * 附件类型 DOC
     */
    public static final String ATTACH_TYPE_DOC = "doc";
    
    /**
     * 附件类型 PPT
     */
    public static final String ATTACH_TYPE_PPT = "ppt";
    
    /**
     * 附件类型 XLS
     */
    public static final String ATTACH_TYPE_XLS = "xls";
    
    /**
     * 附件类型 TXT
     */
    public static final String ATTACH_TYPE_TXT = "txt";
    
    /**
     * 附件类型 XLS
     */
    public static final String ATTACH_TYPE_RAR = "rar";
    
    /**
     * 附件类型 HTML
     */
    public static final String ATTACH_TYPE_HTML = "html";
    
    /**
     * 附件类型 AUDIO
     */
    public static final String ATTACH_TYPE_AUDIO = "audio";
    
    /**
     * 附件类型 文件
     */
    public static final String ATTACH_TYPE_FILE = "file";
    
    /**
     * TAG
     */
    private static final String TAG = FileUtil.class.getSimpleName();
    
    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 100 * 1024;
    
    private static final String[][] MIME_MAPTABLE = {
            //{后缀名，MIME类型} 
            { ".3gp", "video/3gpp" },
            { ".apk", "application/vnd.android.package-archive" },
            { ".asf", "video/x-ms-asf" },
            { ".avi", "video/x-msvideo" },
            { ".bin", "application/octet-stream" },
            { ".bmp", "image/bmp" },
            { ".c", "text/plain" },
            { ".class", "application/octet-stream" },
            { ".conf", "text/plain" },
            { ".cpp", "text/plain" },
            { ".doc", "application/msword" },
            { ".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
            { ".xls", "application/vnd.ms-excel" },
            { ".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
            { ".exe", "application/octet-stream" },
            { ".gif", "image/gif" },
            { ".gtar", "application/x-gtar" },
            { ".gz", "application/x-gzip" },
            { ".h", "text/plain" },
            { ".htm", "text/html" },
            { ".html", "text/html" },
            { ".jar", "application/java-archive" },
            { ".java", "text/plain" },
            { ".jpeg", "image/jpeg" },
            { ".jpg", "image/jpeg" },
            { ".js", "application/x-javascript" },
            { ".log", "text/plain" },
            { ".m3u", "audio/x-mpegurl" },
            { ".m4a", "audio/mp4a-latm" },
            { ".m4b", "audio/mp4a-latm" },
            { ".m4p", "audio/mp4a-latm" },
            { ".m4u", "video/vnd.mpegurl" },
            { ".m4v", "video/x-m4v" },
            { ".mov", "video/quicktime" },
            { ".mp2", "audio/x-mpeg" },
            { ".mp3", "audio/x-mpeg" },
            { ".mp4", "video/mp4" },
            { ".mpc", "application/vnd.mpohun.certificate" },
            { ".mpe", "video/mpeg" },
            { ".mpeg", "video/mpeg" },
            { ".mpg", "video/mpeg" },
            { ".mpg4", "video/mp4" },
            { ".mpga", "audio/mpeg" },
            { ".msg", "application/vnd.ms-outlook" },
            { ".ogg", "audio/ogg" },
            { ".pdf", "application/pdf" },
            { ".png", "image/png" },
            { ".pps", "application/vnd.ms-powerpoint" },
            { ".ppt", "application/vnd.ms-powerpoint" },
            { ".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
            { ".prop", "text/plain" }, { ".rc", "text/plain" },
            { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
            { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
            { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
            { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
            { ".wmv", "audio/x-ms-wmv" },
            { ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
            { ".z", "application/x-compress" },
            { ".zip", "application/x-zip-compressed" }, { "", "*/*" } };
    
    /**
     * 获得附件文件类型
     * 
     * @param pathString
     *              文件路径或名称
     * @return
     *              文件类型
     */
    public static String getAttachType(String pathString)
    {
        String typeString = FileUtil.getFileTypeString(pathString);
        if (typeString.toLowerCase().equals("png")
                || typeString.toLowerCase().equals("jpg")
                || typeString.toLowerCase().equals("jpeg")
                || typeString.toLowerCase().equals("bmp")
                || typeString.toLowerCase().equals("gif"))
        {
            return ATTACH_TYPE_PIC;
        }
        else if (typeString.toLowerCase().equals("pdf"))
        {
            return ATTACH_TYPE_PDF;
        }
        else if (typeString.toLowerCase().equals("vob")
                || typeString.toLowerCase().equals("avi")
                || typeString.toLowerCase().equals("rm")
                || typeString.toLowerCase().equals("rmvb")
                || typeString.toLowerCase().equals("mp4")
                || typeString.toLowerCase().equals("3gp"))
        {
            return ATTACH_TYPE_VIDEO;
        }
        else if (typeString.toLowerCase().equals("doc")
                || typeString.toLowerCase().equals("docx"))
        {
            return ATTACH_TYPE_DOC;
        }
        else if (typeString.toLowerCase().equals("ppt")
                || typeString.toLowerCase().equals("pptx"))
        {
            return ATTACH_TYPE_PPT;
        }
        else if (typeString.toLowerCase().equals("xls")
                || typeString.toLowerCase().equals("xlsx"))
        {
            return ATTACH_TYPE_XLS;
        }
        else if (typeString.toLowerCase().equals("txt"))
        {
            return ATTACH_TYPE_TXT;
        }
        else if (typeString.toLowerCase().equals("mkv")
                || typeString.toLowerCase().equals("avi")
                || typeString.toLowerCase().equals("rm")
                || typeString.toLowerCase().equals("rmvb")
                || typeString.toLowerCase().equals("mp4")
                || typeString.toLowerCase().equals("flv"))
        {
            return ATTACH_TYPE_VIDEO;
        }
        else if (typeString.toLowerCase().equals("rar")
                || typeString.toLowerCase().equals("zip"))
        {
            return ATTACH_TYPE_RAR;
        }
        else if (typeString.toLowerCase().equals("html")
                || typeString.toLowerCase().equals("mht"))
        {
            return ATTACH_TYPE_HTML;
        }
        else if (typeString.toLowerCase().equals("mp3")
                || typeString.toLowerCase().equals("wma")
                || typeString.toLowerCase().equals("wav"))
        {
            return ATTACH_TYPE_AUDIO;
        }
        else
        {
            return ATTACH_TYPE_FILE;
        }
    }
    
    /**
     *
     * 文件转化byte[]操作
     *
     * @param file 需要转化为byte[]的文件
     * @return 文件的byte[]格式
     * @throws IOException IO流异常
     */
    public static byte[] fileToByte(File file) throws IOException
    {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try
        {
            byte[] barr = new byte[1024];
            while (true)
            {
                int r = in.read(barr);
                if (r <= 0)
                {
                    break;
                }
                buffer.write(barr, 0, r);
            }
            return buffer.toByteArray();
        }
        finally
        {
            buffer.close();
            closeStream(in);
        }
    }
    
    /**
     * 复制文件
     * @param srcFilePath 源文件路劲
     * @param dstFilePath 目标文件路劲
     * @return true:复制成功 ; false:复制失败
     */
    public static boolean fileRW(String srcFilePath, String dstFilePath)
    {
        
        if (TextUtils.isEmpty(srcFilePath) || TextUtils.isEmpty(dstFilePath))
        {
            Log.e(TAG, "srcFilePath or dstFilePath is null");
            return false;
        }
        
        FileInputStream inputStream = null;
        
        FileOutputStream outputStream = null;
        
        try
        {
            
            inputStream = new FileInputStream(srcFilePath);
            
            File file = new File(dstFilePath);
            
            File fileTemp = file.getParentFile();
            
            if (null != fileTemp && !fileTemp.exists())
            {
                fileTemp.mkdirs();
            }
            
            outputStream = new FileOutputStream(dstFilePath);
            byte[] b = new byte[1024 * 1024];
            
            int count;
            
            while ((count = inputStream.read(b)) != -1)
            {
                outputStream.write(b);
                Log.d(TAG, "count=" + count);
            }
            
            outputStream.flush();
            
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.d(TAG, "e=" + e.toString());
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d(TAG, "e=" + e.toString());
            return false;
        }
        finally
        {
            try
            {
                if (null != outputStream)
                {
                    outputStream.close();
                }
                
                if (null != inputStream)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.d(TAG, "e=" + e.toString());
                return false;
            }
        }
        return true;
    }
    
    /**
     * 拼装出欲设定的文件名称:原始文件名_YYYYMMDDHHMMSS.扩展名
     * 
     * @param timeString
     *              YYYYMMDDHHMMSS时间字符串
     * @param orgFileName
     *              原始文件的路径或者名称,以获得文件后缀名
     * @return
     *              拼装出欲设定的文件名称:原始文件名_YYYYMMDDHHMMSS.扩展名
     */
    public static String getCopyFileName(String timeString, String orgFileName)
    {
        String copyFileName = "";
        //文件后缀名
        String fileType = getFileTypeString(orgFileName);
        String fileName = getFileName(orgFileName);
        
        if (!TextUtils.isEmpty(fileType))
        {
            copyFileName = fileName + "_" + timeString + "." + fileType;
        }
        else
        {
            copyFileName = fileName + "_" + timeString;
        }
        return copyFileName;
    }
    
    /**
     * 文件转化byte[]操作
     * @param fileName 文件路径
     * @return 文件的byte[]格式
     */
    public static byte[] fileToByte(String fileName)
    {
        try
        {
            return fileToByte(new File(fileName));
        }
        catch (IOException e)
        {
            Log.e(TAG, "fileToByte error", e);
        }
        return null;
    }
    
    /**
     *
     * 将文件的byte[]格式转化成一个文件
     *
     * @param b 文件的byte[]格式
     * @param fileName 文件名称
     * @return 转化后的文件
     */
    public static File byteToFile(byte[] b, String fileName)
    {
        BufferedOutputStream bos = null;
        File file = null;
        // ***BEGIN*** [ST2-图片读取管理] 王玉丰 2012-8-6 modify
        // 增加文件锁处理
        FileLock fileLock = null;
        try
        {
            file = new File(fileName);
            if (!file.exists())
            {
                File parent = file.getParentFile();
                // 此处增加判断parent != null && !parent.exists() -- by 王玉丰
                if (parent != null && !parent.exists() && !parent.mkdirs())
                {
                    // 创建不成功的话，直接返回null
                    return null;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            // 获取文件锁
            fileLock = fos.getChannel().tryLock();
            if (fileLock != null)
            {
                bos = new BufferedOutputStream(fos);
                bos.write(b);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "byteToFile error", e);
        }
        finally
        {
            if (fileLock != null)
            {
                try
                {
                    fileLock.release();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "fileLock.release() error");
                }
            }
            // ***END***  [ST2-图片读取管理] 王玉丰 2012-8-6 modify
            closeStream(bos);
        }
        return file;
    }
    
    /**
    *
    * 将文件的byte[]格式转化成一个文件(IO异常则将文件删除)
    *
    * @param b 文件的byte[]格式
    * @param fileName 文件名称
    * @return 成功失败标志
    */
    public static boolean byteToFileBoolean(byte[] b, String fileName)
    {
        BufferedOutputStream bos = null;
        File file = null;
        boolean flag = true;
        try
        {
            file = new File(fileName);
            if (!file.exists())
            {
                File parent = file.getParentFile();
                if (!parent.exists())
                {
                    if (!parent.mkdirs())
                    {
                        // 创建不成功的话，直接返回null
                        return false;
                    }
                }
                
            }
            
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(b);
        }
        catch (IOException e)
        {
            flag = false;
            //如果写文件失败，则将文件删除
            if (file != null)
            {
                file.delete();
            }
            Log.e(TAG, "byteToFile error", e);
        }
        catch (Exception e)
        {
            Log.e(TAG, "byteToFile error  exception", e);
        }
        finally
        {
            closeStream(bos);
        }
        return flag;
    }
    
    /**
     * 判断文件是否是图片格式
     *
     * @param fileName 文件名称
     * @return true 表示是图片格式 false 表示不是图片格式
     */
    public static boolean isPictureType(String fileName)
    {
        int index = fileName.lastIndexOf(".");
        if (index != -1)
        {
            String type = fileName.substring(index).toLowerCase();
            if (".png".equals(type) || ".gif".equals(type)
                    || ".jpg".equals(type) || ".bmp".equals(type)
                    || ".jpeg".equals(type))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 判断文件是否是视频格式
     *
     * @param fileName 文件名称
     * @return true 表示是视频格式 false 表示不是视频格式
     */
    public static boolean isVideoType(String fileName)
    {
        String type = getFileTypeString(fileName);
        return "mp4".equals(type) || "3gp".equals(type) || "rmvb".equals(type);
    }
    
    /**
     * 删除文件
     *
     * @param path 路径
     * @return 是否即时删除成功
     */
    public static boolean deleteFiles(String path)
    {
        if (path == null || path.trim().length() < 1)
        {
            return false;
        }
        try
        {
            File file = new File(path);
            if (file != null && file.exists() && file.isFile())
            {
                try
                {
                    return file.delete();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "delete file error", e);
                    file.deleteOnExit();
                }
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return 是否即时删除成功
     */
    public static boolean deleteFile(File file)
    {
        try
        {
            if (file != null && file.exists() && file.isFile())
            {
                try
                {
                    return file.delete();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "delete file error", e);
                    file.deleteOnExit();
                }
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 专门用来关闭可关闭的流
     *
     * @param beCloseStream 需要关闭的流
     * @return 已经为空或者关闭成功返回true，否则返回false
     */
    public static boolean closeStream(java.io.Closeable beCloseStream)
    {
        if (beCloseStream != null)
        {
            try
            {
                beCloseStream.close();
                return true;
            }
            catch (IOException e)
            {
                Log.e(TAG, "close stream error", e);
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     * 根据文件完整路径获取文件名（包含后缀）
     * @param filePath 文件完整路径
     * @return 文件名
     */
    public static String getFileNameWithSuffix(String filePath)
    {
        String fileName;
        if (filePath == null || filePath.trim().length() < 1)
        {
            fileName = null;
        }
        try
        {
            File file = new File(filePath);
            fileName = file.getName();
        }
        catch (Exception e)
        {
            fileName = null;
        }
        return fileName;
    }
    
    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static long getFileLength(String filePath)
    {
        if (filePath == null || filePath.trim().length() < 1)
        {
            return 0;
        }
        try
        {
            File file = new File(filePath);
            return file.length();
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * 复制文件
     *
     * @param origin 原始文件
     * @param dest 目标文件
     * @return 是否复制成功
     */
    public static boolean copyFile(File origin, File dest)
    {
        if (origin == null || dest == null)
        {
            return false;
        }
        if (!dest.exists())
        {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists())
            {
                boolean succeed = parentFile.mkdirs();
                if (!succeed)
                {
                    Log.i(TAG, "copyFile failed, cause mkdirs return false");
                    return false;
                }
            }
            try
            {
                dest.createNewFile();
            }
            catch (Exception e)
            {
                Log.i(TAG, "copyFile failed, cause createNewFile failed");
                return false;
            }
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = new FileInputStream(origin);
            out = new FileOutputStream(dest);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            int length = BUFFER_SIZE;
            while (true)
            {
                if (inC.position() == inC.size())
                {
                    return true;
                }
                if ((inC.size() - inC.position()) < BUFFER_SIZE)
                {
                    length = (int) (inC.size() - inC.position());
                }
                else
                {
                    length = BUFFER_SIZE;
                }
                inC.transferTo(inC.position(), length, outC);
                inC.position(inC.position() + length);
            }
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            closeStream(in);
            closeStream(out);
        }
    }
    
    /**
     * 复制文件
     *
     * @param rid 资源ID
     * @param context Context
     * @param dest 目标文件
     * @return 是否复制成功
     */
    public static boolean copyFile(int rid, Context context, File dest)
    {
        if (context == null || dest == null)
        {
            return false;
        }
        if (!dest.exists())
        {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists())
            {
                boolean succeed = parentFile.mkdirs();
                if (!succeed)
                {
                    Log.i(TAG, "copyFile failed, cause mkdirs return false");
                    return false;
                }
            }
            try
            {
                dest.createNewFile();
            }
            catch (Exception e)
            {
                Log.i(TAG, "copyFile failed, cause createNewFile failed");
                return false;
            }
        }
        InputStream is = null;
        OutputStream os = null;
        try
        {
            is = context.getResources().openRawResource(rid);
            os = context.openFileOutput(dest.getName(), Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            //将raw文件copy到/data/data/应用包名/files/下
            while (is.read(buffer) != -1)
            {
                os.write(buffer);
            }
            return true;
        }
        catch (FileNotFoundException e)
        {
            Log.d(TAG, "ring file not exists");
            return false;
        }
        catch (IOException e)
        {
            Log.d(TAG, e.getMessage());
            return false;
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    Log.d(TAG, "raw voip_ring.wav close exception");
                }
            }
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    Log.d(TAG, "data voip_ring.wav close exception");
                }
            }
        }
    }
    
    /**
     * 移动文件(剪切)
     *
     * @param origin 原始文件
     * @param dest 目标文件
     * @return true: 剪切成功;false:剪切失败
     */
    public static boolean moveFile(File origin, File dest)
    {
        if (origin == null || dest == null)
        {
            return false;
        }
        if (!dest.exists())
        {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists())
            {
                boolean succeed = parentFile.mkdirs();
                if (!succeed)
                {
                    Log.i(TAG, "copyFile failed, cause mkdirs return false");
                    return false;
                }
            }
            try
            {
                dest.createNewFile();
            }
            catch (Exception e)
            {
                Log.i(TAG, "copyFile failed, cause createNewFile failed");
                return false;
            }
        }
        
        //尝试直接剪切
        boolean isSuc = origin.renameTo(dest);
        
        //剪切不成功,尝试复制
        if (!isSuc)
        {
            return fileRW(origin.getAbsolutePath(), dest.getAbsolutePath());
        }
        
        //成功返回成功
        return isSuc;
    }
    
    /**
     * 判断SD卡上是否有合适的容量（5M）<BR>
     *
     * @return 是/否
     */
    public static boolean isSuitableSizeForSDCard()
    {
        return isSuitableSizeForSDCard(5 * 1024 * 1024);
    }
    
    /**
     * 
     * 判断sdcard是否有足够的空间
     * @param size 需要的大小
     * @return boolean
     */
    public static boolean isSuitableSizeForSDCard(long size)
    {
        boolean result = false;
        String sdcardState = Environment.getExternalStorageState();
        if (StringUtil.equals(sdcardState, Environment.MEDIA_MOUNTED))
        {
            File sdCardDir = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(sdCardDir.getPath());
            long nBlockSize = statfs.getBlockSize();
            long nAvailaBlock = statfs.getAvailableBlocks();
            long nSDFreeSize = nBlockSize * nAvailaBlock;
            if (nSDFreeSize > size)
            {
                result = true;
            }
            Log.d(TAG, "SD卡剩余容量为： " + nSDFreeSize + "B");
        }
        return result;
    }
    
    /**
     * 设置没有媒体文件标志
     * @param directory 文件
     * @return 是否有媒体文件
     */
    public static boolean setNoMediaFlag(File directory)
    {
        File noMediaFile = new File(directory, ".nomedia");
        if (!noMediaFile.exists())
        {
            try
            {
                return noMediaFile.createNewFile();
            }
            catch (IOException e)
            {
                return false;
            }
        }
        return false;
    }
    
    /**
     * 通过提供的文件名在默认路径下生成文件
     * @param filePath 文件的名称
     * @return 生成的文件
     * @throws IOException IOException
     */
    public static File createFile(String filePath) throws IOException
    {
        String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
        File folder = getFileByPath(folderPath);
        folder.mkdirs();
        File file = getFileByPath(filePath);
        if (!file.exists())
        {
            file.createNewFile();
        }
        else
        {
            return createFile(getNextPath(filePath));
        }
        return file;
    }
    
    /**
     * 已经有文件名相同文件时 文件名后加1
     * @param path 文件路径
     * @return 新生成的文件路径
     */
    private static String getNextPath(String path)
    {
        Pattern pattern = Pattern.compile("\\(\\d{1,}\\)\\.");
        //除中文不用外，其他的都要 
        Matcher matcher = pattern.matcher(path);
        String str = null;
        while (matcher.find())
        {
            str = matcher.group(matcher.groupCount());
        }
        
        if (str == null)
        {
            int index = path.lastIndexOf(".");
            if (index != -1)
            {
                path = path.substring(0, index) + "(1)" + path.substring(index);
            }
            else
            {
                path += "(1)";
            }
        }
        else
        {
            int index = Integer.parseInt(str.replaceAll("[^\\d]*(\\d)[^\\d]*",
                    "$1")) + 1;
            path = path.replace(str, "(" + index + ").");
        }
        return path;
    }
    
    /**
     * 删除路径指向的文件
     * @param  filePath   文件的名称
     * @return true删除成功，false删除失败
     */
    public static boolean deleteFile(String filePath)
    {
        File file = getFileByPath(filePath);
        if (null != file && file.isFile())
        {
            return file.delete();
        }
        return false;
    }
    
    /**
     * 删除文件夹下的所有文件<BR>
     * @param folderFullPath 文件夹路径
     */
    public static void deleteAllFile(String folderFullPath)
    {
        File file = new File(folderFullPath);
        if (file.exists())
        {
            if (file.isDirectory())
            {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++)
                {
                    String filePath = fileList[i].getPath();
                    deleteAllFile(filePath);
                }
            }
            if (file.isFile())
            {
                //异常控制 当文件正在被操作 无法删除时 用OnExit进行删除
                try
                {
                    file.delete();
                }
                catch (Exception e)
                {
                    file.deleteOnExit();
                }
            }
        }
    }
    
    /**
     * 获取文件路径
     * @param filePath String
     * @return 文件路径
     */
    public static File getFileByPath(String filePath)
    {
        filePath = filePath.replaceAll("\\\\", "/");
        boolean isSdcard = false;
        int subIndex = 0;
        if (filePath.indexOf("/sdcard") == 0)
        {
            isSdcard = true;
            subIndex = 7;
        }
        else if (filePath.indexOf("/mnt/sdcard") == 0)
        {
            isSdcard = true;
            subIndex = 11;
        }
        
        if (isSdcard)
        {
            if (isExistSdcard())
            {
                //获取SDCard目录,2.2的时候为:/mnt/sdcard  2.1的时候为：/sdcard，所以使用静态方法得到路径会好一点。
                File sdCardDir = Environment.getExternalStorageDirectory();
                String fileName = filePath.substring(subIndex);
                return new File(sdCardDir, fileName);
            }
            else if (isEmulator())
            {
                File sdCardDir = Environment.getExternalStorageDirectory();
                String fileName = filePath.substring(subIndex);
                return new File(sdCardDir, fileName);
            }
            return null;
        }
        else
        {
            return new File(filePath);
        }
    }
    
    /**
     * 是否有sdcard
     * @return boolean
     */
    public static boolean isExistSdcard()
    {
        if (!isEmulator())
        {
            return Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);
        }
        return true;
    }
    
    /**
     * 是否是模拟器
     * @return boolean
     */
    public static boolean isEmulator()
    {
        return android.os.Build.MODEL.equals("sdk");
    }
    
    /**
     * 根据文件名获取不带后缀的文件名字符串
     * 
     * @param fileName 文件名
     * @return 文件名
     */
    public static String getFileName(String fileName)
    {
        if (TextUtils.isEmpty(fileName))
        {
            return "";
        }
        int dotIdx = fileName.lastIndexOf(".");
        if (dotIdx != -1)
        {
            return fileName.substring(0, dotIdx);
        }
        else if (fileName.indexOf(".") == -1)
        {
            return fileName;
        }
        return "";
    }
    
    /**
     * 获得文件的后缀名
     *
     * @param fileName 
     *              文件名称
      * @return
      *             文件不带点的后缀名,没有后缀名返回""
      */
    public static String getFileTypeString(String fileName)
    {
        if (TextUtils.isEmpty(fileName))
        {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1)
        {
            String type = fileName.substring(index + 1).toLowerCase();
            return type;
        }
        else
        {
            return "";
        }
    }
    
    /**
     * 获取文件类型<BR>
     * 用于获取文件类型
     * @param fileName 文件名
     * @return 返回文件类型
     */
    public static String getMIMEType(String fileName)
    {
        
        String type = "*/*";
        //获取后缀名前的分隔符"."在fName中的位置。 
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0)
        {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fileName.substring(dotIndex, fileName.length())
                .toLowerCase();
        if (end == "")
        {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。 
        for (int i = 0; i < FileUtil.MIME_MAPTABLE.length; i++)
        { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？ 
            if (end.equals(FileUtil.MIME_MAPTABLE[i][0]))
            {
                type = MIME_MAPTABLE[i][1];
            }
        }
        return type;
    }
    
    /**
     * 通过文件路径获得文件大小显示字串
     * 
     * @param filePath
     *              文件路径
     * @return
     *              文件大小显示字串
     */
    public static String getFileSizeFromPath(String filePath)
    {
        //格式化小数，不足的补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        
        double mBSize = 1024 * 1024.0;
        int kBSize = 1024;
        int bSize = 1;
        long fileLength = FileUtil.getFileLength(filePath);
        if (fileLength >= mBSize)
        {
            return decimalFormat.format(fileLength / mBSize) + "MB";
        }
        else if (fileLength >= kBSize)
        {
            return Long.valueOf(fileLength / kBSize) + "KB";
        }
        else if (fileLength >= bSize)
        {
            return Long.valueOf(fileLength / bSize) + "B";
        }
        else
        {
            return "0B";
        }
    }
    
    /**
     * 通过文件路径获得文件大小显示字串
     * @param size
     *              文件大小
     * @return
     *              文件大小
     */
    public static String getFileSizeFromIntSize(int size)
    {
        //格式化小数，不足的补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        
        double mBSize = 1024 * 1024.0;
        int kBSize = 1024;
        int bSize = 1;
        if (size >= mBSize)
        {
            return decimalFormat.format(size / mBSize) + "MB";
        }
        else if (size >= kBSize)
        {
            return Integer.valueOf(size / kBSize) + "KB";
        }
        else if (size >= bSize)
        {
            return Integer.valueOf(size / bSize) + "B";
        }
        else
        {
            return "0B";
        }
    }
    
    /**
     * 
     * 判断指定路径下文件是否存在<BR>
     * @param filePath 文件路径
     * @return true 存在，false 不存在
     */
    public static boolean isFileExists(String filePath)
    {
        File file = getFileByPath(filePath);
        return file == null ? false : file.exists();
    }
    
    /**
     * 
     * 判断制定路径下的文件是否被改变过<BR>
     * @param filePath 文件路径
     * @return true 改变过，false没改变
     */
    public static boolean isFileChanged(String filePath)
    {
        //TODO 该方法需完善
        boolean isChanged = false;
        File file = getFileByPath(filePath);
        //        long lastTime = file.lastModified();
        return isChanged;
    }
}