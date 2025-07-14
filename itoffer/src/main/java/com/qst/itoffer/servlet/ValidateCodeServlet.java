package com.qst.itoffer.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码图像生成
 *
 * @author QST青软实训
 *
 */
@WebServlet("/ValidateCodeServlet")
public class ValidateCodeServlet extends HttpServlet {
    // static{
    // System.setProperty("java.awt.headless","true");//
    // }
    private static final long serialVersionUID = 1L;

    public ValidateCodeServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应头 Content-type类型
        response.setContentType("image/jpeg");
        // 获取二进制数据输出流对象
        ServletOutputStream out = response.getOutputStream();
        HttpSession session = request.getSession();
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            s.append(getRandomChineseChar());
        }
        Random r = new Random();
        String path= getServletContext().getRealPath("/")+"images\\backGround" +r.nextInt(4) + ".jpg";;
        File f = new File(path);
        CreateImage(out, f, s.toString(), session);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public Color getRandColor(int fc, int bc) {
        if (fc < 0)
            fc = 0;
        if (fc > 255)
            fc = 255;
        if (bc < 0)
            bc = 0;
        if (bc > 255)
            bc = 255;
        Random random = new Random();
        int r = fc + (random.nextInt(bc - fc));
        int g = fc + (random.nextInt(bc - fc));
        int b = fc + (random.nextInt(bc - fc));
        return new Color(r, g, b);
    }

    protected void CreateImage(OutputStream out, File file, String s, HttpSession session)
            throws IOException {
        Random r = new Random();
        // 创建缓冲图象
        int width = 600;
        int height = 250;
        int index=r.nextInt(s.length());

        BufferedImage imgbuf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = imgbuf.createGraphics();
        graphics2D.drawImage(ImageIO.read(file), 0, 0, 600, 200, null);
        graphics2D.setColor(new Color(255,255,255));
        graphics2D.fillRect(0, 200, 600, 100);
        int[][] points = new int[s.length()][2];
        Font f=getFont();
        for (int i = 0; i < s.length(); i++) {
            f=f.deriveFont(Font.BOLD,24+r.nextInt(16));
            graphics2D.setFont(f);
            int x = r.nextInt(width-40);
            if (x < 36)
                x += 36;
            int y = r.nextInt(height-140);
            if (y < 36)
                y += 36;
            graphics2D.setColor(getRandColor(50, 150));
            graphics2D.drawString(String.valueOf(s.charAt(i)), x, y);
            points[i][0] = x;
            points[i][1] = y;
        }
        f=f.deriveFont(Font.BOLD,30);
        graphics2D.setFont(f);
        graphics2D.setColor(new Color(0,0,0));
        graphics2D.drawString("请点击图中的",150,240);
        graphics2D.setColor(new Color(255,0,0));
        graphics2D.drawString(s.charAt(index)+"", 340, 240);
        session.setAttribute("Character",s.charAt(index));
        session.setAttribute("xy", points[index]);
        System.out.println("Code x:" + points[index][0] + "y:" + points[index][1]);
        System.out.println("char:" + s.charAt(index));

        ImageIO.write(imgbuf, "JPEG", out);
        out.close();
    }

    public String getRandomChineseChar() {
        String str = null;
        int hs, ls;
        Random random = new Random();
        hs = (176 + Math.abs(random.nextInt(39)));
        ls = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (new Integer(hs).byteValue());
        b[1] = (new Integer(ls).byteValue());
        try {
            str = new String(b, "GBK"); // 转成中文
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return str;
    }

    private Font getFont(){
        Font font = null;
        String path = getServletContext().getRealPath("/")+ "fonts/MSYH.TTC";
        BufferedInputStream bf = null;
        try {
            bf = new BufferedInputStream(new FileInputStream(path));
            
                font = Font.createFont(Font.TRUETYPE_FONT, bf);
            } catch (FontFormatException e) {
                e.printStackTrace();
            } 
            catch (FileNotFoundException e) {
            e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return font;
            
        
    }

    
}