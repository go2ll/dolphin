package com.yttimes.dolphin.definition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefinitionCache {

  private static Map<Long, ProcessDefinition>
      definitionCache =
      Collections.synchronizedMap(new HashMap<Long, ProcessDefinition>());

  private DefinitionCache() {
  }

  public static ProcessDefinition getProcessDefinition(Long processDefId) {
    return definitionCache.get(processDefId);
  }

  public static void put(ProcessDefinition def) {
    definitionCache.put(def.getId(), def);
  }

}
