package com.example.springbatchdemo.batch.demo1;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class CSVResourcePartitioner implements Partitioner {
    private Resource[] resources;
    private String keyName = "DEFAULT_KEY_NAME";

    public CSVResourcePartitioner() {
    }

    public CSVResourcePartitioner(Resource[] resources) {
        this.resources = resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public Map<String, ExecutionContext> partition(int partitionCount) {
        Map<String, ExecutionContext> map = new HashMap<>(partitionCount);
        int i = 0;
        for(Resource resource: resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);
            context.putString("inputFileName", resource.getFilename());
            context.putString("outputFileName", "file" + i + ".csv");
            map.put("partition" + i, context);
            i++;
        }
        return map;
    }
}
