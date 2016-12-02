package com.papa.park.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    public static int parseInt(String string) {
        int result = 0;
        try {
            result = Integer.parseInt(string);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }


    public static int parseInt(Integer integer) {
        int result = 0;
        if (integer == null)
            return result;
        return integer.intValue();
    }

    public static long parseLong(Long lon) {
        Long result = 0l;
        if (lon == null)
            return result;
        return lon;
    }

    public static Long parseLong(String value) {
        long result;
        try {
            result = Long.parseLong(value);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static String parseString(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object);
    }

    public static float parseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static BigDecimal parseBigDecimal(String string) {
        try {
            BigDecimal result = new BigDecimal(string);
            return result;
        } catch (Exception ignored) {
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal parseBigDecimal(float price) {
        try {
            return new BigDecimal(price);
        } catch (Exception ignored) {
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal parseBigDecimal(double price) {
        try {
            return new BigDecimal(price);
        } catch (Exception ignored) {
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal parsePrice(String price) {
        try {
            return new BigDecimal(price);
        } catch (Exception ignored) {
        }
        return BigDecimal.ZERO;
    }

    public static double parseDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static boolean isAllChinese(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        str = str.replaceAll(" ", "");
        char[] charArray = str.toCharArray();
        boolean result = true;
        for (char c : charArray) {
            if (!(c >= 0x4e00) && (c <= 0x9fbb)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean compareZero(String value) {
        return parseBigDecimal(value).compareTo(BigDecimal.ZERO) > 0;
    }

    public static String getValue(String... values) {
        if (values != null) {
            for (String item : values)
                if (!TextUtils.isEmpty(item))
                    return item;
        }
        return "";
    }

    // 去掉所有空格
    public static String replaceAllBlank(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll(" ", "");
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if (input == null || input.length() <= 0) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /**
     * 处理null错误
     *
     * @param str
     * @return
     */
    public static String nullToString(String str) {
        return str == null ? "" : str;
    }

    /**
     * 判断输入的字符是否begin-end位
     * 中文占2位
     *
     * @param begin 开始位置
     * @param end   结束位置
     */
    public static boolean isCharCount(String input, int begin, int end) {
        try {
            String regex = "[\u4e00-\u9fa5]";
            int chineseCount = (" " + input + " ").split(regex).length - 1;
            if (end % 2 == 0) {
                if (chineseCount > end / 2) {
                    return false;
                }
            } else {
                if (chineseCount > ((end / 2) + 1)) {
                    return false;
                }
            }
            int tolChar = (chineseCount * 2) + (input.length() - chineseCount);
            if (tolChar <= end && tolChar >= begin) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否是emoji表情
     *
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * 中文占2位
     */
    public static int charCount(String input) {
        try {
            String regex = "[\u4e00-\u9fa5]";
            int chineseCount = (" " + input + " ").split(regex).length - 1;
            int tolChar = (chineseCount * 2) + (input.length() - chineseCount);
            return tolChar;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 处理非 数字，下划线，中英文
     */
    public static String replaceAllReg(String text) {
        try {
            String reg = "[^a-zA-Z0-9\u4e00-\u9fa5_]";
            text = text.replaceAll(reg, "");
            return text;
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 处理是否是 英文字母，数字，@，_，6-20位
     */
    public static boolean replaceAllRegPassword(String text) {
        try {
            String reg = "[a-zA-Z0-9@_]{6,20}";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断email
     */
    public static boolean isEmail(String text) {
        try {
            String reg = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
            Pattern pattern = Pattern.compile(reg);
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            // 字符串是否与正则表达式相匹配
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验手机号码
     */
    public static boolean isPhoneMobile(String text) {
        try {
            String reg = "0?(13|14|15|17|18)[0-9]{9}";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 隐藏部分手机号
     */
    public static String shieldingString(String text) {
        try {
            return text.replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})", "*");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytesParseMb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "M");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    public static long parseLong2(Long lon) {
        if (lon == null)
            return 0l;
        return lon.longValue();
    }

    /**
     * 获取文本字节长度(中文算两个字节)
     *
     * @param etstring
     * @return
     */
    public static int calculateLength(String etstring) {
        char[] ch = etstring.toCharArray();

        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            // changed by zyf 0825 , bug 6918，加入中文标点范围 ， TODO 标点范围有待具体化
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00 0x9fbb
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        // 这里也可以使用getBytes,更准确嘛
        // varlength = etstring.getBytes(CharSet.forName("GBK")).lenght;// 编码根据自己的需求，注意u8中文占3个字节...
        return varlength;
    }

    /**
     * 正则表达式去掉小数点后面多余的0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy2ClipBoard(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String getTextFromClipBoard(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().getItemAt(0).getText().toString().trim();
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
