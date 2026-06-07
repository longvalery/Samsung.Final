package rva.com.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FontBuilder {
    private static final String ALL_CHARS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        + "0123456789.,!?-_/:;()[]{}" // цифры и знаки препинания
        + getAllEnglishChars();

    public static String getAllEnglishChars() {
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) { sb.append(c); }
        for (char c = 'A'; c <= 'Z'; c++) { sb.append(c); }
        return sb.toString();
    }

    public static BitmapFont generate(int size, Color color, String fontPath) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        parameter.characters = ALL_CHARS;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }


    public static class FontWithShader {
        public BitmapFont font;
        public ShaderProgram shader;
    }

    public static FontWithShader createTwoColorFont(int size, String fontPath) {
        FontWithShader result = new FontWithShader();

        // 1. Генерируем БЕЛЫЙ шрифт через FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = "ПриветABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789"; // нужные символы
        parameter.color = Color.WHITE;   // обязательно белый!
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        result.font = generator.generateFont(parameter);
        generator.dispose();

        // 2. Создаём шейдер (без изменений, он рабочий)
        String vertexShader =
            "attribute vec4 a_position;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec2 v_texCoords;\n" +
                "varying vec2 v_worldPos;\n" +
                "void main() {\n" +
                "   v_texCoords = a_texCoord0;\n" +
                "   v_worldPos = a_position.xy;\n" +
                "   gl_Position = u_projTrans * a_position;\n" +
                "}\n";

        String fragmentShader =
            "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec2 v_texCoords;\n" +
                "varying vec2 v_worldPos;\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float u_diagonalAngle;\n" +
                "uniform float u_diagonalOffset;\n" +
                "void main() {\n" +
                "   vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                "   if (texColor.a < 0.05) discard;\n" +
                "   float lineValue = v_worldPos.y + u_diagonalAngle * v_worldPos.x - u_diagonalOffset;\n" +
                "   vec3 black = vec3(0.0, 0.0, 0.0);\n" +
                "   vec3 red = vec3(1.0, 0.0, 0.0);\n" +
                "   vec3 finalColor = mix(red, black, step(0.0, lineValue));\n" +
                "   gl_FragColor = vec4(finalColor, texColor.a);\n" +
                "}\n";

        result.shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!result.shader.isCompiled()) {
            Gdx.app.error("Shader", result.shader.getLog());
        }
        // Устанавливаем постоянный угол (45°)
        result.shader.begin();
        result.shader.setUniformf("u_diagonalAngle", 1.0f);
        result.shader.end();

        return result;
    }

    public static ShaderProgram generateShader(int size, String fontPath) {
        // Генерация шрифта через FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = ALL_CHARS;
        parameter.color = Color.WHITE;   // базовый цвет (будет заменён шейдером)
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        String vertexShader =
            "attribute vec4 a_position;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec2 v_texCoords;\n" +
                "varying vec2 v_worldPos;\n" +
                "void main() {\n" +
                "   v_texCoords = a_texCoord0;\n" +
                "   v_worldPos = a_position.xy;\n" +
                "   gl_Position = u_projTrans * a_position;\n" +
                "}\n";

        String fragmentShader =
            "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec2 v_texCoords;\n" +
                "varying vec2 v_worldPos;\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float u_diagonalAngle;\n" +
                "uniform float u_diagonalOffset;\n" +
                "void main() {\n" +
                "   vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                "   if (texColor.a < 0.05) discard;\n" +
                "   float lineValue = v_worldPos.y + u_diagonalAngle * v_worldPos.x - u_diagonalOffset;\n" +
                "   vec3 black = vec3(0.0, 0.0, 0.0);\n" +
                "   vec3 red = vec3(1.0, 0.0, 0.0);\n" +
                "   vec3 finalColor = mix(red, black, step(0.0, lineValue));\n" +
                "   gl_FragColor = vec4(finalColor, texColor.a);\n" +
                "}\n";

//        // Создаём шейдер
//        String vertexShader =
//            "attribute vec4 a_position;\n" +
//                "attribute vec2 a_texCoord0;\n" +
//                "uniform mat4 u_projTrans;\n" +
//                "varying vec2 v_texCoords;\n" +
//                "varying vec2 v_worldPos;\n" +
//                "void main() {\n" +
//                "   v_texCoords = a_texCoord0;\n" +
//                "   vec4 worldPos = a_position;\n" +
//                "   v_worldPos = worldPos.xy;\n" +
//                "   gl_Position = u_projTrans * worldPos;\n" +
//                "}\n";
//
//        String fragmentShader =
//            "#ifdef GL_ES\n" +
//                "precision mediump float;\n" +
//                "#endif\n" +
//                "varying vec2 v_texCoords;\n" +
//                "varying vec2 v_worldPos;\n" +
//                "uniform sampler2D u_texture;\n" +
//                "uniform float u_diagonalAngle;     // наклон диагонали (tan угла), по умолчанию 1.0 (45°)\n" +
//                "uniform float u_diagonalOffset;    // смещение линии\n" +
//                "void main() {\n" +
//                "   vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
//                "   if (texColor.a < 0.05) discard;\n" +
//                "   // уравнение линии: y = -u_diagonalAngle * x + offset\n" +
//                "   float lineValue = v_worldPos.y + u_diagonalAngle * v_worldPos.x - u_diagonalOffset;\n" +
//                "   vec3 black = vec3(0.0, 0.0, 0.0);\n" +
//                "   vec3 red = vec3(1.0, 0.0, 0.0);\n" +
//                "   vec3 finalColor = mix(red, black, step(0.0, lineValue));\n" +
//                "   gl_FragColor = vec4(finalColor, texColor.a);\n" +
//                "}\n";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.error("Shader", shader.getLog());
        }

        // Устанавливаем uniform-параметры диагонали
        shader.begin();
        shader.setUniformf("u_diagonalAngle", 1.0f);   // наклон 45°
        // Смещение подбираем так, чтобы линия пересекала текст по центру (зависит от позиции отрисовки)
        shader.setUniformf("u_diagonalOffset", 0f);
        shader.end();
        return shader;
    }

}


