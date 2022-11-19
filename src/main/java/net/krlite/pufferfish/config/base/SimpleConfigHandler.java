package net.krlite.pufferfish.config.base;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SimpleConfigHandler implements SimpleConfig.DefaultConfig {
    private final List<Pair> configListStatic = new ArrayList<>(), configList = new ArrayList<>();
    private final ArrayList<String> keyList = new ArrayList<>();

    public List<Pair> getConfigList(boolean isStatic) {
        return isStatic ? configListStatic : configList;
    }

    private void appendConfigList(Pair<?, String> keyValuePair) {
        configList.add(keyValuePair);
    }

    private void syncConfigList() {
        configList.clear();
        configList.addAll(configListStatic);
    }

    public void addCategory(String category) {
        keyList.add("__category_" + category.toLowerCase());
        configListStatic.add(new Pair<>("__category", category));
        appendConfigList(new Pair<>("__category", category));
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

        configList.set(index, new Pair<>(keyValuePair.getSecond(), configList.get(index).getSecond()));
    }

    public String generateConfigContents() {
        StringBuilder configContents = new StringBuilder();

        for ( String key : keyList) {
            int index = keyList.indexOf(key);

            if ( key.startsWith("__category") ) {
                String category = (String) configListStatic.get(index).getSecond();

                configContents
                        .append("#| ")
                        .append(configListStatic.get(index).getSecond())
                        .append(" |\n\n");
            } else {
                configContents
                        .append(key)
                        .append("=")
                        .append(configList.get(index).getFirst())
                        .append("\n#value: ")
                        .append(configList.get(index).getSecond())
                        .append(" | default: ")
                        .append(configListStatic.get(index).getFirst())
                        .append("\n\n");
            }
        }

        return configContents.toString();
    }

    @Override
    public String get(String namespace) {
        return generateConfigContents();
    }
}
