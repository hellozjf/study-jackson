package com.hellozjf.study.studyjackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class StudyJacksonApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyJacksonApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ObjectMapper objectMapper) {
        return args -> {
            try {
                string2Student(objectMapper);
                student2String(objectMapper);
                student2File(objectMapper);
                file2Student(objectMapper);
                data2String(objectMapper);
                string2Data(objectMapper);
                string2Object(objectMapper);
                string2JsonNode(objectMapper);
                jsonNode2String(objectMapper);
                jsonNode2UserData(objectMapper);
                jsonGenerator(objectMapper);
                jsonParser(objectMapper);
            } catch (Exception e) {
                log.error("e = {}", e);
            }
        };
    }

    /**
     * 字符串转化为Java对象
     *
     * @param objectMapper
     * @throws Exception
     */
    private void string2Student(ObjectMapper objectMapper) throws Exception {
        String string = "{\"name\":\"Mahesh\", \"age\":21}";
        Student student = objectMapper.readValue(string, Student.class);
        log.debug("string2Student string={} student={}", string, student);
    }

    /**
     * Java对象转化为字符串
     *
     * @param objectMapper
     * @throws Exception
     */
    private void student2String(ObjectMapper objectMapper) throws Exception {
        Student student = new Student("hellozjf", 28);
        String string = objectMapper.writeValueAsString(student);
        log.debug("student2String student={} string={}", student, string);
    }

    /**
     * Java对象存储为Json文件
     *
     * @param objectMapper
     * @throws Exception
     */
    private void student2File(ObjectMapper objectMapper) throws Exception {
        Student student = new Student("hellozjf", 28);
        objectMapper.writeValue(new File("student.json"), student);
    }

    /**
     * 从Json文件中读取Java对象
     *
     * @param objectMapper
     * @throws Exception
     */
    private void file2Student(ObjectMapper objectMapper) throws Exception {
        Student student = objectMapper.readValue(new File("student.json"), Student.class);
        log.debug("file2Student student={}", student);
    }

    /**
     * 数据绑定，将Map、List、String、Integer、Double、Boolean、Null转化为Json相应字符串
     *
     * @param objectMapper
     * @throws Exception
     */
    private void data2String(ObjectMapper objectMapper) throws Exception {
        Map<String, Object> studentDataMap = new HashMap<>();
        studentDataMap.put("student", new Student("hellozjf", 28));
        studentDataMap.put("name", "Mahesh Kumar");
        studentDataMap.put("money", 123.45);
        studentDataMap.put("sex", null);
        studentDataMap.put("verified", true);
        studentDataMap.put("marks", new int[]{1, 2, 3});
        String json = objectMapper.writeValueAsString(studentDataMap);
        log.debug("data2String\n\tdata={}\n\tstring={}", studentDataMap, json);
    }

    /**
     * 数据绑定，将Json相应字符串转化为Map、List、String、Integer、Double、Boolean、Null
     *
     * @param objectMapper
     * @throws Exception
     */
    private void string2Data(ObjectMapper objectMapper) throws Exception {
        String string = "{\"money\":123.45,\"student\":{\"name\":\"hellozjf\",\"age\":28},\"sex\":null,\"name\":\"Mahesh Kumar\",\"verified\":true,\"marks\":[1,2,3]}";
        Map<String, Object> map = objectMapper.readValue(string, Map.class);
        log.debug("string2Data\n\tstring={}\n\tdata={}", string, map);
    }

    /**
     * 数据绑定，将Json相应字符串转化为对象或对象数组
     *
     * @param objectMapper
     * @throws Exception
     */
    private void string2Object(ObjectMapper objectMapper) throws Exception {
        String string = "{\"money\":123.45,\"student\":{\"name\":\"hellozjf\",\"age\":28},\"sex\":null,\"name\":\"Mahesh Kumar\",\"verified\":true,\"marks\":[1,2,3]}";
        UserData userData = objectMapper.readValue(string, new TypeReference<UserData>() {
        });
        log.debug("string2Object\n\tstring={}\n\tdata={}", string, userData);

        string = "[" + string + "," + string + "]";
        List<UserData> userDataList = objectMapper.readValue(string, new TypeReference<List<UserData>>() {
        });
        log.debug("string2Object\n\tstring={}\n\tdata={}", string, userDataList);
    }

    /**
     * 将JSON字符串转化为Jackson树模型
     *
     * @param objectMapper
     * @throws Exception
     */
    private void string2JsonNode(ObjectMapper objectMapper) throws Exception {
        String string = "{\"name\":\"Mahesh Kumar\", \"age\":21,\"verified\":false,\"marks\": [100,90,85]}";
        JsonNode jsonNode = objectMapper.readTree(string);

        JsonNode nameNode = jsonNode.path("name");
        log.debug("\tnameNode={}", nameNode.textValue());

        JsonNode ageNode = jsonNode.path("age");
        log.debug("\tageNode={}", ageNode.intValue());

        JsonNode verifiedNode = jsonNode.path("verified");
        log.debug("\tverifiedNode={}", verifiedNode.booleanValue());

        JsonNode marksNode = jsonNode.path("marks");
        log.debug("\tmarksNode=");
        Iterator iter = marksNode.iterator();
        while (iter.hasNext()) {
            log.debug("\t\t{}", iter.next());
        }
    }

    /**
     * 将Jackson树模型转化为JSON字符串
     *
     * @param objectMapper
     * @throws Exception
     */
    private void jsonNode2String(ObjectMapper objectMapper) throws Exception {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("name", "Mahesh Kumar");
        rootNode.put("age", 21);
        rootNode.put("verified", false);
        ArrayNode arrayNode = rootNode.putArray("marks");
        arrayNode.add(100);
        arrayNode.add(95);
        arrayNode.add(90);

        String string = objectMapper.writeValueAsString(rootNode);
        log.debug("jsonNode2String string={}", string);
    }

    /**
     * 将Jackson树模型转化为UserData类型
     *
     * @param objectMapper
     * @throws Exception
     */
    private void jsonNode2UserData(ObjectMapper objectMapper) throws Exception {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode objectNode = rootNode.putObject("student");
        objectNode.put("name", "hellozjf");
        objectNode.put("age", 18);
        rootNode.put("name", "hellozjf2");
        rootNode.put("money", 123.456);
        rootNode.putNull("sex");
        rootNode.put("verified", false);
        ArrayNode arrayNode = rootNode.putArray("marks");
        arrayNode.add(100);
        arrayNode.add(95);
        arrayNode.add(90);

        UserData userData = objectMapper.treeToValue(rootNode, UserData.class);
        log.debug("jsonNode2UserData userData={}", userData);
    }

    /**
     * 通过jsonGenerator生成JSON字符串
     *
     * @param objectMapper
     * @throws Exception
     */
    private void jsonGenerator(ObjectMapper objectMapper) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteArrayOutputStream, JsonEncoding.UTF8);

        // {
        jsonGenerator.writeStartObject();
        // "name" : "Mahesh Kumar"
        jsonGenerator.writeStringField("name", "周靖峰");
        // "age" : 21
        jsonGenerator.writeNumberField("age", 21);
        // "verified" : false
        jsonGenerator.writeBooleanField("verified", false);
        // "marks" : [100, 90, 85]
        jsonGenerator.writeFieldName("marks");
        // [
        jsonGenerator.writeStartArray();
        // 100, 90, 85
        jsonGenerator.writeNumber(100);
        jsonGenerator.writeNumber(90);
        jsonGenerator.writeNumber(85);
        // ]
        jsonGenerator.writeEndArray();
        // }
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String string = byteArrayOutputStream.toString();
        log.debug("jsonGenerator string={}", string);
    }

    /**
     * 使用JsonParser读取JSON字符串
     * @param objectMapper
     * @throws Exception
     */
    private void jsonParser(ObjectMapper objectMapper) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{\"name\":\"周靖峰\",\"age\":21,\"verified\":false,\"marks\":[100,90,85]}");
        log.debug("jsonParser:");
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            //get the current token
            String fieldname = jsonParser.getCurrentName();
            if ("name".equals(fieldname)) {
                //move to next token
                jsonParser.nextToken();
                log.debug("\tname={}", jsonParser.getText());
            }
            if("age".equals(fieldname)){
                //move to next token
                jsonParser.nextToken();
                log.debug("\tage={}", jsonParser.getNumberValue());
            }
            if("verified".equals(fieldname)){
                //move to next token
                jsonParser.nextToken();
                log.debug("\tverified={}", jsonParser.getBooleanValue());
            }
            if("marks".equals(fieldname)){
                //move to [
                jsonParser.nextToken();
                log.debug("\tmarks=");
                // loop till token equal to "]"
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    log.debug("\t\t{}", jsonParser.getNumberValue());
                }
            }
        }
    }
}

