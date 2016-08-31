package de.jablab.sebschlicht.kits.resources;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import de.jablab.sebschlicht.kits.model.Series;

/**
 * {@link ResourceProvider} that wraps the other resource providers and thus
 * uses all data sources that this application can connect to.
 *
 * @author sebschlicht
 *
 */
public class AnyResourceProvider implements ResourceProvider {

    /**
     * underlying resource providers
     */
    private List<ResourceProvider> resourceProviders;

    /**
     * Creates a wrapping resource provider for a number of resource providers.
     * The wrapper will use these resource providers as its data source.
     *
     * @param resourceProviders
     *            resource providers this resource provider should wrap
     */
    public AnyResourceProvider(
            final List<ResourceProvider> resourceProviders) {
        this.resourceProviders = resourceProviders;
    }

    @Override
    public boolean hasAudioFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasAudioFile(series)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasImageFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasImageFile(series)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasVideoFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasVideoFile(series)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public File getAudioFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasAudioFile(series)) {
                return resourceProvider.getAudioFile(series);
            }
        }
        return null;
    }

    @Override
    public File getImageFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasImageFile(series)) {
                return resourceProvider.getImageFile(series);
            }
        }
        return null;
    }

    @Override
    public File getVideoFile(final Series series) {
        for (ResourceProvider resourceProvider : this.resourceProviders) {
            if (resourceProvider.hasVideoFile(series)) {
                return resourceProvider.getVideoFile(series);
            }
        }
        return null;
    }

    @Override
    public Uri getAudioFileUri(final Series series) {
        return StorageProvider.uriFromFile(this.getAudioFile(series));
    }

    @Override
    public Uri getImageFileUri(final Series series) {
        return StorageProvider.uriFromFile(this.getImageFile(series));
    }

    @Override
    public Uri getVideoFileUri(final Series series) {
        return StorageProvider.uriFromFile(this.getVideoFile(series));
    }

    /**
     * Creates a resource provider wrapping all other resource providers,
     * thus using all data sources that this application can connect to.
     *
     * @param context
     *            application context
     * @return resource provider wrapping all other resource providers
     */
    public static AnyResourceProvider create(final Context context) {
        final List<ResourceProvider> resourceProviders =
                new LinkedList<ResourceProvider>();
        // add all known resource providers available
        resourceProviders.add(InternalStorageProvider.create(context));
        if (ExternalStorageProvider.isExternalStorageReadable()) {
            resourceProviders.add(ExternalStorageProvider.create(context));
        }
        resourceProviders.add(RawResourceProvider.create(context));
        return new AnyResourceProvider(resourceProviders);
    }
}
