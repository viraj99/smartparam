package org.smartparam.repository.fs.resolver;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.StandardSerializationConfig;
import org.smartparam.serializer.StandardParamDeserializer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.googlecode.catchexception.CatchException.*;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FileResourceResolverIntegrationTest extends ResolverInegrationTestConsts {

    private static final String PARAMETER_DIR_NAME = "smartParam";

    private String basePath;

    private FileResourceResolver resolver;

    @DataProvider(name = "parameterResourceToNameDataProvider")
    public Object[][] parameterResourceToNameDataProvider() {
        return new Object[][]{
            {"param1", 1, createFilePath(basePath) + "param1.csv"},
            {"param2", 2, createFilePath(basePath, "param") + "param2.csv"},
            {"param3", 3, createFilePath(basePath, "param", "deep") + "param3.csv"}
        };
    }

    @BeforeClass
    public void setUpTest() throws Exception {
        File tempPath = Files.createTempDirectory(PARAMETER_DIR_NAME).toFile();
        basePath = tempPath.toString();

        new File(tempPath, createFilePath("param", "deep")).mkdirs();

        File param1TmpFile = new File(tempPath, "param1.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param1.csv"), param1TmpFile);

        File param2TmpFile = new File(tempPath, createFilePath("param") + "param2.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param/param2.csv"), param2TmpFile);

        File param3TmpFile = new File(tempPath, createFilePath("param", "deep") + "param3.csv");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param/deep/param3.csv"), param3TmpFile);

        File param4TmpFile = new File(tempPath, "param4_ignored.txt");
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/param4_ignored.txt"), param4TmpFile);
    }

    @AfterClass
    public void tearDownTest() throws Exception {
        FileUtils.forceDelete(new File(basePath));
    }

    @BeforeMethod
    public void setUp() {
        SerializationConfig config = new StandardSerializationConfig();
        ParamDeserializer deserializer = new StandardParamDeserializer(
                config,
                SimpleEditableParameter.class, SimpleEditableLevel.class, SimpleEditableParameterEntry.class);

        resolver = new FileResourceResolver(basePath, ".*csv$", deserializer);
    }

    @Test
    public void shouldReturnMappingOfParametersOntoTheirLocationsRelativeToBasePath() throws Exception {
        // given

        // when
        Map<String, String> parameters = resolver.findParameterResources();

        // then
        assertThat(parameters).hasSize(3).contains(
                entry("param1", createFilePath(basePath) + "param1.csv"),
                entry("param2", createFilePath(basePath, "param") + "param2.csv"),
                entry("param3", createFilePath(basePath, "param", "deep") + "param3.csv"))
                .doesNotContain(entry("param4_ignored", createFilePath(basePath) + "param4_ignored.txt"));
    }

    @Test(dataProvider = "parameterResourceToNameDataProvider")
    public void shouldReturnParameterFromRepository(String parameterName, int expectedSize, String parameterResource) throws Exception {
        // given

        // when
        Parameter parameter = resolver.loadParameterFromResource(parameterResource);

        // then
        assertThat(parameter).hasName(parameterName).hasEntries(expectedSize);
    }

    @Test
    public void shouldBailIfUnresolvableResource() {
        // given

        // when
        catchException(resolver).loadParameterFromResource("WRONG_RESOURCE_NAME");

        // then
        assertThat(caughtException()).isInstanceOf(SmartParamResourceResolverException.class);
    }
}
