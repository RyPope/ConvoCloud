package ryan.pope.textcloud.cloud.padding;

import ryan.pope.textcloud.cloud.objects.Word;

public interface Padder {
    void pad(final Word word, final int padding);
}
