package net.krlite.pufferfish.config.simple_config;

import com.mojang.datafixers.util.Pair;
import net.krlite.pufferfish.config.simple_config.SimpleConfig;

import java.util.ArrayList;
import java.util.List;

public class SimpleConfigHandler implements SimpleConfig.DefaultConfig {
    private final List<Pair> configListStatic = new ArrayList<>(), configList = new ArrayList<>();
    private final ArrayList<String> keyList = new ArrayList<>();

    public List<Pair> getConfigList(boolean isStatic) {
        return isStatic ? configListStatic : configList;
    }

    public void syncConfigList() {
        configList.clear();
        configList.addAll(configListStatic);
    }

    /**
     * Appends config with comment.
     *
     * @param key           key of the dedicated config.
     * @param keyValuePair  a pair of (?) config value and (String) comment.
     */
    public void addConfig(String key, Pair<?, String> keyValuePair) {
        keyList.add(key);
        configListStatic.add(keyValuePair);
        syncConfigList();
    }

    /**
     * Changes config value by key.
     *
     * @param keyValuePair a pair of (String) config key and (?) config value.
     */
    public void modifyConfig(Pair<String, ?> keyValuePair) {
        int index = keyList.indexOf(keyValuePair.getFirst());

        configList.set(index, new Pair(keyValuePair.getSecond(), configList.get(index).getSecond()));
    }

    public String generateConfigContents() {
        StringBuilder configContents = new StringBuilder();

        for ( String key : keyList) {
            int index = keyList.indexOf(key);

            configContents.append(
                    key + "=" + configList.get(index).getFirst() +
                            "\n#value: " + configList.get(index).getSecond() +
                            " | default: " + configListStatic.get(index).getFirst() + "\n\n"
            );
        }

        return configContents.toString();
    }

    @Override
    public String get(String namespace) {
        return generateConfigContents();
    }
}
