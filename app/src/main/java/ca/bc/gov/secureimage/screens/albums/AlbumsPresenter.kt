package ca.bc.gov.secureimage.screens.albums

import ca.bc.gov.secureimage.data.models.local.Album
import ca.bc.gov.secureimage.data.repos.albums.AlbumsRepo
import ca.bc.gov.secureimage.data.repos.locationrepo.LocationRepo
import com.github.florent37.rxgps.RxGps
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Aidan Laing on 2017-12-12.
 *
 */
class AlbumsPresenter(
        private val view: AlbumsContract.View,
        private val albumsRepo: AlbumsRepo,
        private val locationRepo: LocationRepo,
        private val rxGps: RxGps
) : AlbumsContract.Presenter {

    private val disposables = CompositeDisposable()

    init {
        view.presenter = this
    }

    override fun subscribe() {
        view.hideLoading()
        view.hideOnboarding()

        view.setUpSettingsListener()

        view.setUpAlbumsList()
        view.setUpCreateAlbumListener()
    }

    override fun dispose() {
        disposables.dispose()
    }

    override fun viewShown() {
        getLocationAndCache()
        getAlbums()
    }

    override fun viewHidden() {
        disposables.clear()
    }

    override fun settingsClicked() {
        view.goToSettings()
    }

    /**
     * Grabs user location and caches it in location repo
     */
    fun getLocationAndCache() {
        locationRepo.getLocation(rxGps, true)
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {})
                .addTo(disposables)
    }

    /**
     * Grabs all the albums with preview image from the albums repo
     * Orders albums and displays in view
     * On Success shows items and displays help text if no albums exist
     */
    fun getAlbums() {
        view.showAlbumItems(ArrayList())
        view.showLoading()
        albumsRepo.getAllAlbums()
                .flatMapIterable { it }
                .toSortedList { album1, album2 -> album1.compareTo(album2) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onError = {
                    view.hideLoading()
                    view.showError(it.message ?: "Error retrieving albums")
                },
                onSuccess = { albums ->
                    val items = ArrayList<Any>()
                    items.addAll(albums)
                    view.hideLoading()

                    if (items.isEmpty()) {
                        view.showOnboarding()
                    } else {
                        view.showAlbumItems(items)
                        view.hideOnboarding()
                    }
                })
                .addTo(disposables)
    }

    // Create album
    override fun createAlbumClicked() {
        createAlbum()
    }

    /**
     * Creates an album with default values and goes to the create album page on success
     */
    fun createAlbum() {
        albumsRepo.createAlbum()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onError = {
                    view.showError(it.message ?: "Error creating album")
                },
                onSuccess = { album ->
                    view.goToCreateAlbum(album.key)
                })
                .addTo(disposables)
    }

    // Album clicks
    override fun albumClicked(album: Album) {
        view.goToCreateAlbum(album.key)
    }
}