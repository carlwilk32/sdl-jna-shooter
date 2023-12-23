package com.github.carlwilk32.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.ptr.PointerByReference;
import com.github.carlwilk32.api.render.SDL_Renderer;
import com.github.carlwilk32.api.render.SDL_Texture;
import com.github.carlwilk32.api.rwops.SDL_RWops;
import com.github.carlwilk32.api.surface.SDL_Surface;
import com.github.carlwilk32.api.version.SDL_version;
import org.intellij.lang.annotations.MagicConstant;

/**
 * JNA Wrapper for library <b>SDL_image_copy</b>: A simple library to load images of various formats
 * as SDL surfaces<br>
 */
public interface SDL_imageLibrary extends Library {
  String JNA_LIBRARY_NAME = "SDL2_image";
  NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(SDL_imageLibrary.JNA_LIBRARY_NAME);
  SDL_imageLibrary INSTANCE =
      Native.load(SDL_imageLibrary.JNA_LIBRARY_NAME, SDL_imageLibrary.class);

  int SDL_IMAGE_MAJOR_VERSION = 2;
  int SDL_IMAGE_MINOR_VERSION = 9;
  int SDL_IMAGE_PATCHLEVEL = 0;

  /**
   * This function gets the version of the dynamically linked SDL_image library.<br>
   * it should NOT be used to fill a version structure, instead you should use<br>
   * the SDL_IMAGE_VERSION() macro.<br>
   * \returns SDL_image version<br>
   * Original signature : <code>SDL_version* IMG_Linked_Version()</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:7</i>
   */
  SDL_version IMG_Linked_Version();

  /**
   * Initialize SDL_image.<br>
   * This function loads dynamic libraries that SDL_image needs, and prepares<br>
   * them for use. This must be the first function you call in SDL_image, and if<br>
   * it fails you should not continue with the library.<br>
   * Flags should be one or more flags from IMG_InitFlags OR'd together. It<br>
   * returns the flags successfully initialized, or 0 on failure.<br>
   * Currently, these flags are:<br>
   * - `IMG_INIT_JPG`<br>
   * - `IMG_INIT_PNG`<br>
   * - `IMG_INIT_TIF`<br>
   * - `IMG_INIT_WEBP`<br>
   * - `IMG_INIT_JXL`<br>
   * - `IMG_INIT_AVIF`<br>
   * More flags may be added in a future SDL_image release.<br>
   * This function may need to load external shared libraries to support various<br>
   * codecs, which means this function can fail to initialize that support on an<br>
   * otherwise-reasonable system if the library isn't available; this is not<br>
   * just a question of exceptional circumstances like running out of memory at<br>
   * startup!<br>
   * Note that you may call this function more than once to initialize with<br>
   * additional flags. The return value will reflect both new flags that<br>
   * successfully initialized, and also include flags that had previously been<br>
   * initialized as well.<br>
   * As this will return previously-initialized flags, it's legal to call this<br>
   * with zero (no flags set). This is a safe no-op that can be used to query<br>
   * the current initialization state without changing it at all.<br>
   * Since this returns previously-initialized flags as well as new ones, and<br>
   * you can call this with zero, you should not check for a zero return value<br>
   * to determine an error condition. Instead, you should check to make sure all<br>
   * the flags you require are set in the return value. If you have a game with<br>
   * data in a specific format, this might be a fatal error. If you're a generic<br>
   * image displaying app, perhaps you are fine with only having JPG and PNG<br>
   * support and can live without WEBP, even if you request support for<br>
   * everything.<br>
   * Unlike other SDL satellite libraries, calls to IMG_Init do not stack; a<br>
   * single call to IMG_Quit() will deinitialize everything and does not have to<br>
   * be paired with a matching IMG_Init call. For that reason, it's considered<br>
   * best practices to have a single IMG_Init and IMG_Quit call in your program.<br>
   * While this isn't required, be aware of the risks of deviating from that<br>
   * behavior.<br>
   * After initializing SDL_image, the app may begin to load images into<br>
   * SDL_Surfaces or SDL_Textures.<br>
   * \param flags initialization flags, OR'd together.<br>
   * \returns all currently initialized flags.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_Quit<br>
   * Original signature : <code>int IMG_Init(int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:66</i>
   */
  int IMG_Init(@MagicConstant(flagsFromClass = IMG_InitFlags.class) int flags);

  /**
   * Deinitialize SDL_image.<br>
   * This should be the last function you call in SDL_image, after freeing all<br>
   * other resources. This will unload any shared libraries it is using for<br>
   * various codecs.<br>
   * After this call, a call to IMG_Init(0) will return 0 (no codecs loaded).<br>
   * You can safely call IMG_Init() to reload various codec support after this<br>
   * call.<br>
   * Unlike other SDL satellite libraries, calls to IMG_Init do not stack; a<br>
   * single call to IMG_Quit() will deinitialize everything and does not have to<br>
   * be paired with a matching IMG_Init call. For that reason, it's considered<br>
   * best practices to have a single IMG_Init and IMG_Quit call in your program.<br>
   * While this isn't required, be aware of the risks of deviating from that<br>
   * behavior.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_Init<br>
   * Original signature : <code>void IMG_Quit()</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:85</i>
   */
  void IMG_Quit();

  /**
   * Load an image from an SDL data source into a software surface.<br>
   * An SDL_Surface is a buffer of pixels in memory accessible by the CPU. Use<br>
   * this if you plan to hand the data to something else or manipulate it<br>
   * further in code.<br>
   * There are no guarantees about what format the new SDL_Surface data will be;<br>
   * in many cases, SDL_image will attempt to supply a surface that exactly<br>
   * matches the provided image, but in others it might have to convert (either<br>
   * because the image is in a format that SDL doesn't directly support or<br>
   * because it's compressed data that could reasonably uncompress to various<br>
   * formats and SDL_image had to pick one). You can inspect an SDL_Surface for<br>
   * its specifics, and use SDL_ConvertSurface to then migrate to any supported<br>
   * format.<br>
   * If the image format supports a transparent pixel, SDL will set the colorkey<br>
   * for the surface. You can enable RLE acceleration on the surface afterwards<br>
   * by calling: SDL_SetColorKey(image, SDL_RLEACCEL, image->format->colorkey);<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * Even though this function accepts a file type, SDL_image may still try<br>
   * other decoders that are capable of detecting file type from the contents of<br>
   * the image data, but may rely on the caller-provided type string for formats<br>
   * that it cannot autodetect. If `type` is NULL, SDL_image will rely solely on<br>
   * its ability to guess the format.<br>
   * There is a separate function to read files from disk without having to deal<br>
   * with SDL_RWops: `IMG_Load("filename.jpg")` will call this function and<br>
   * manage those details for you, determining the file type from the filename's<br>
   * extension.<br>
   * There is also IMG_Load_RW(), which is equivalent to this function except<br>
   * that it will rely on SDL_image to determine what type of data it is<br>
   * loading, much like passing a NULL for type.<br>
   * If you are using SDL's 2D rendering API, there is an equivalent call to<br>
   * load images directly into an SDL_Texture for use by the GPU without using a<br>
   * software surface: call IMG_LoadTextureTyped_RW() instead.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \param type a filename extension that represent this data ("BMP", "GIF",<br>
   * "PNG", etc).<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_Load<br>
   * \sa IMG_Load_RW<br>
   * \sa SDL_FreeSurface<br>
   * Original signature : <code>SDL_Surface* IMG_LoadTyped_RW(SDL_RWops*, int, const char*)</code>
   * <br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:134</i>
   */
  SDL_Surface IMG_LoadTyped_RW(SDL_RWops src, int freesrc, String type);

  /**
   * Load an image from a filesystem path into a software surface.<br>
   * An SDL_Surface is a buffer of pixels in memory accessible by the CPU. Use<br>
   * this if you plan to hand the data to something else or manipulate it<br>
   * further in code.<br>
   * There are no guarantees about what format the new SDL_Surface data will be;<br>
   * in many cases, SDL_image will attempt to supply a surface that exactly<br>
   * matches the provided image, but in others it might have to convert (either<br>
   * because the image is in a format that SDL doesn't directly support or<br>
   * because it's compressed data that could reasonably uncompress to various<br>
   * formats and SDL_image had to pick one). You can inspect an SDL_Surface for<br>
   * its specifics, and use SDL_ConvertSurface to then migrate to any supported<br>
   * format.<br>
   * If the image format supports a transparent pixel, SDL will set the colorkey<br>
   * for the surface. You can enable RLE acceleration on the surface afterwards<br>
   * by calling: SDL_SetColorKey(image, SDL_RLEACCEL, image->format->colorkey);<br>
   * There is a separate function to read files from an SDL_RWops, if you need<br>
   * an i/o abstraction to provide data from anywhere instead of a simple<br>
   * filesystem read; that function is IMG_Load_RW().<br>
   * If you are using SDL's 2D rendering API, there is an equivalent call to<br>
   * load images directly into an SDL_Texture for use by the GPU without using a<br>
   * software surface: call IMG_LoadTexture() instead.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param file a path on the filesystem to load an image from.<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadTyped_RW<br>
   * \sa IMG_Load_RW<br>
   * \sa SDL_FreeSurface<br>
   * Original signature : <code>SDL_Surface* IMG_Load(const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:167</i>
   */
  SDL_Surface IMG_Load(String file);

  /**
   * Load an image from an SDL data source into a software surface.<br>
   * An SDL_Surface is a buffer of pixels in memory accessible by the CPU. Use<br>
   * this if you plan to hand the data to something else or manipulate it<br>
   * further in code.<br>
   * There are no guarantees about what format the new SDL_Surface data will be;<br>
   * in many cases, SDL_image will attempt to supply a surface that exactly<br>
   * matches the provided image, but in others it might have to convert (either<br>
   * because the image is in a format that SDL doesn't directly support or<br>
   * because it's compressed data that could reasonably uncompress to various<br>
   * formats and SDL_image had to pick one). You can inspect an SDL_Surface for<br>
   * its specifics, and use SDL_ConvertSurface to then migrate to any supported<br>
   * format.<br>
   * If the image format supports a transparent pixel, SDL will set the colorkey<br>
   * for the surface. You can enable RLE acceleration on the surface afterwards<br>
   * by calling: SDL_SetColorKey(image, SDL_RLEACCEL, image->format->colorkey);<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * There is a separate function to read files from disk without having to deal<br>
   * with SDL_RWops: `IMG_Load("filename.jpg")` will call this function and<br>
   * manage those details for you, determining the file type from the filename's<br>
   * extension.<br>
   * There is also IMG_LoadTyped_RW(), which is equivalent to this function<br>
   * except a file extension (like "BMP", "JPG", etc) can be specified, in case<br>
   * SDL_image cannot autodetect the file format.<br>
   * If you are using SDL's 2D rendering API, there is an equivalent call to<br>
   * load images directly into an SDL_Texture for use by the GPU without using a<br>
   * software surface: call IMG_LoadTexture_RW() instead.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_Load<br>
   * \sa IMG_LoadTyped_RW<br>
   * \sa SDL_FreeSurface<br>
   * Original signature : <code>SDL_Surface* IMG_Load_RW(SDL_RWops*, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:209</i>
   */
  SDL_Surface IMG_Load_RW(SDL_RWops src, int freesrc);

  /**
   * Load an image from a filesystem path into a GPU texture.<br>
   * An SDL_Texture represents an image in GPU memory, usable by SDL's 2D Render<br>
   * API. This can be significantly more efficient than using a CPU-bound<br>
   * SDL_Surface if you don't need to manipulate the image directly after<br>
   * loading it.<br>
   * If the loaded image has transparency or a colorkey, a texture with an alpha<br>
   * channel will be created. Otherwise, SDL_image will attempt to create an<br>
   * SDL_Texture in the most format that most reasonably represents the image<br>
   * data (but in many cases, this will just end up being 32-bit RGB or 32-bit<br>
   * RGBA).<br>
   * There is a separate function to read files from an SDL_RWops, if you need<br>
   * an i/o abstraction to provide data from anywhere instead of a simple<br>
   * filesystem read; that function is IMG_LoadTexture_RW().<br>
   * If you would rather decode an image to an SDL_Surface (a buffer of pixels<br>
   * in CPU memory), call IMG_Load() instead.<br>
   * When done with the returned texture, the app should dispose of it with a<br>
   * call to SDL_DestroyTexture().<br>
   * \param renderer the SDL_Renderer to use to create the GPU texture.<br>
   * \param file a path on the filesystem to load an image from.<br>
   * \returns a new texture, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadTextureTyped_RW<br>
   * \sa IMG_LoadTexture_RW<br>
   * \sa SDL_DestroyTexture<br>
   * Original signature : <code>SDL_Texture* IMG_LoadTexture(SDL_Renderer*, const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:237</i>
   */
  SDL_Texture IMG_LoadTexture(SDL_Renderer renderer, String file);

  /**
   * Load an image from an SDL data source into a GPU texture.<br>
   * An SDL_Texture represents an image in GPU memory, usable by SDL's 2D Render<br>
   * API. This can be significantly more efficient than using a CPU-bound<br>
   * SDL_Surface if you don't need to manipulate the image directly after<br>
   * loading it.<br>
   * If the loaded image has transparency or a colorkey, a texture with an alpha<br>
   * channel will be created. Otherwise, SDL_image will attempt to create an<br>
   * SDL_Texture in the most format that most reasonably represents the image<br>
   * data (but in many cases, this will just end up being 32-bit RGB or 32-bit<br>
   * RGBA).<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * There is a separate function to read files from disk without having to deal<br>
   * with SDL_RWops: `IMG_LoadTexture(renderer, "filename.jpg")` will call this<br>
   * function and manage those details for you, determining the file type from<br>
   * the filename's extension.<br>
   * There is also IMG_LoadTextureTyped_RW(), which is equivalent to this<br>
   * function except a file extension (like "BMP", "JPG", etc) can be specified,<br>
   * in case SDL_image cannot autodetect the file format.<br>
   * If you would rather decode an image to an SDL_Surface (a buffer of pixels<br>
   * in CPU memory), call IMG_Load() instead.<br>
   * When done with the returned texture, the app should dispose of it with a<br>
   * call to SDL_DestroyTexture().<br>
   * \param renderer the SDL_Renderer to use to create the GPU texture.<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \returns a new texture, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadTexture<br>
   * \sa IMG_LoadTextureTyped_RW<br>
   * \sa SDL_DestroyTexture<br>
   * Original signature : <code>SDL_Texture* IMG_LoadTexture_RW(SDL_Renderer*, SDL_RWops*, int)
   * </code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:274</i>
   */
  SDL_Texture IMG_LoadTexture_RW(SDL_Renderer renderer, SDL_RWops src, int freesrc);

  /**
   * Load an image from an SDL data source into a GPU texture.<br>
   * An SDL_Texture represents an image in GPU memory, usable by SDL's 2D Render<br>
   * API. This can be significantly more efficient than using a CPU-bound<br>
   * SDL_Surface if you don't need to manipulate the image directly after<br>
   * loading it.<br>
   * If the loaded image has transparency or a colorkey, a texture with an alpha<br>
   * channel will be created. Otherwise, SDL_image will attempt to create an<br>
   * SDL_Texture in the most format that most reasonably represents the image<br>
   * data (but in many cases, this will just end up being 32-bit RGB or 32-bit<br>
   * RGBA).<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * Even though this function accepts a file type, SDL_image may still try<br>
   * other decoders that are capable of detecting file type from the contents of<br>
   * the image data, but may rely on the caller-provided type string for formats<br>
   * that it cannot autodetect. If `type` is NULL, SDL_image will rely solely on<br>
   * its ability to guess the format.<br>
   * There is a separate function to read files from disk without having to deal<br>
   * with SDL_RWops: `IMG_LoadTexture("filename.jpg")` will call this function<br>
   * and manage those details for you, determining the file type from the<br>
   * filename's extension.<br>
   * There is also IMG_LoadTexture_RW(), which is equivalent to this function<br>
   * except that it will rely on SDL_image to determine what type of data it is<br>
   * loading, much like passing a NULL for type.<br>
   * If you would rather decode an image to an SDL_Surface (a buffer of pixels<br>
   * in CPU memory), call IMG_LoadTyped_RW() instead.<br>
   * When done with the returned texture, the app should dispose of it with a<br>
   * call to SDL_DestroyTexture().<br>
   * \param renderer the SDL_Renderer to use to create the GPU texture.<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \param type a filename extension that represent this data ("BMP", "GIF",<br>
   * "PNG", etc).<br>
   * \returns a new texture, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadTexture<br>
   * \sa IMG_LoadTexture_RW<br>
   * \sa SDL_DestroyTexture<br>
   * Original signature : <code>
   * SDL_Texture* IMG_LoadTextureTyped_RW(SDL_Renderer*, SDL_RWops*, int, const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:318</i>
   */
  SDL_Texture IMG_LoadTextureTyped_RW(
      SDL_Renderer renderer, SDL_RWops src, int freesrc, String type);

  /**
   * Detect AVIF image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is AVIF data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isAVIF(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:355</i>
   */
  int IMG_isAVIF(SDL_RWops src);

  /**
   * Detect ICO image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is ICO data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isICO(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:391</i>
   */
  int IMG_isICO(SDL_RWops src);

  /**
   * Detect CUR image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is CUR data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isCUR(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:427</i>
   */
  int IMG_isCUR(SDL_RWops src);

  /**
   * Detect BMP image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is BMP data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isBMP(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:463</i>
   */
  int IMG_isBMP(SDL_RWops src);

  /**
   * Detect GIF image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is GIF data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isGIF(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:499</i>
   */
  int IMG_isGIF(SDL_RWops src);

  /**
   * Detect JPG image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is JPG data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isJPG(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:535</i>
   */
  int IMG_isJPG(SDL_RWops src);

  /**
   * Detect JXL image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is JXL data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isJXL(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:571</i>
   */
  int IMG_isJXL(SDL_RWops src);

  /**
   * Detect LBM image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is LBM data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isLBM(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:607</i>
   */
  int IMG_isLBM(SDL_RWops src);

  /**
   * Detect PCX image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is PCX data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isPCX(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:643</i>
   */
  int IMG_isPCX(SDL_RWops src);

  /**
   * Detect PNG image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is PNG data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isPNG(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:679</i>
   */
  int IMG_isPNG(SDL_RWops src);

  /**
   * Detect PNM image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is PNM data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isPNM(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:715</i>
   */
  int IMG_isPNM(SDL_RWops src);

  /**
   * Detect SVG image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is SVG data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.2.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isSVG(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:751</i>
   */
  int IMG_isSVG(SDL_RWops src);

  /**
   * Detect QOI image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is QOI data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isQOI(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:787</i>
   */
  int IMG_isQOI(SDL_RWops src);

  /**
   * Detect TIFF image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is TIFF data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isTIF(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:823</i>
   */
  int IMG_isTIF(SDL_RWops src);

  /**
   * Detect XCF image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is XCF data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isXCF(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:859</i>
   */
  int IMG_isXCF(SDL_RWops src);

  /**
   * Detect XPM image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is XPM data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXV<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isXPM(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:895</i>
   */
  int IMG_isXPM(SDL_RWops src);

  /**
   * Detect XV image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is XV data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isWEBP<br>
   * Original signature : <code>int IMG_isXV(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:931</i>
   */
  int IMG_isXV(SDL_RWops src);

  /**
   * Detect WEBP image data on a readable/seekable SDL_RWops.<br>
   * This function attempts to determine if a file is a given filetype, reading<br>
   * the least amount possible from the SDL_RWops (usually a few bytes).<br>
   * There is no distinction made between "not the filetype in question" and<br>
   * basic i/o errors.<br>
   * This function will always attempt to seek the RWops back to where it<br>
   * started when this function was called, but it will not report any errors in<br>
   * doing so, but assuming seeking works, this means you can immediately use<br>
   * this with a different IMG_isTYPE function, or load the image without<br>
   * further seeking.<br>
   * You do not need to call this function to load data; SDL_image can work to<br>
   * determine file type in many cases in its standard load functions.<br>
   * \param src a seekable/readable SDL_RWops to provide image data.<br>
   * \returns non-zero if this is WEBP data, zero otherwise.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_isAVIF<br>
   * \sa IMG_isICO<br>
   * \sa IMG_isCUR<br>
   * \sa IMG_isBMP<br>
   * \sa IMG_isGIF<br>
   * \sa IMG_isJPG<br>
   * \sa IMG_isJXL<br>
   * \sa IMG_isLBM<br>
   * \sa IMG_isPCX<br>
   * \sa IMG_isPNG<br>
   * \sa IMG_isPNM<br>
   * \sa IMG_isSVG<br>
   * \sa IMG_isQOI<br>
   * \sa IMG_isTIF<br>
   * \sa IMG_isXCF<br>
   * \sa IMG_isXPM<br>
   * \sa IMG_isXV<br>
   * Original signature : <code>int IMG_isWEBP(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:967</i>
   */
  int IMG_isWEBP(SDL_RWops src);

  /**
   * Load a AVIF image directly.<br>
   * If you know you definitely have a AVIF image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadAVIF_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:997</i>
   */
  SDL_Surface IMG_LoadAVIF_RW(SDL_RWops src);

  /**
   * Load a ICO image directly.<br>
   * If you know you definitely have a ICO image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadICO_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1027</i>
   */
  SDL_Surface IMG_LoadICO_RW(SDL_RWops src);

  /**
   * Load a CUR image directly.<br>
   * If you know you definitely have a CUR image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadCUR_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1057</i>
   */
  SDL_Surface IMG_LoadCUR_RW(SDL_RWops src);

  /**
   * Load a BMP image directly.<br>
   * If you know you definitely have a BMP image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadBMP_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1087</i>
   */
  SDL_Surface IMG_LoadBMP_RW(SDL_RWops src);

  /**
   * Load a GIF image directly.<br>
   * If you know you definitely have a GIF image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadGIF_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1117</i>
   */
  SDL_Surface IMG_LoadGIF_RW(SDL_RWops src);

  /**
   * Load a JPG image directly.<br>
   * If you know you definitely have a JPG image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadJPG_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1147</i>
   */
  SDL_Surface IMG_LoadJPG_RW(SDL_RWops src);

  /**
   * Load a JXL image directly.<br>
   * If you know you definitely have a JXL image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadJXL_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1177</i>
   */
  SDL_Surface IMG_LoadJXL_RW(SDL_RWops src);

  /**
   * Load a LBM image directly.<br>
   * If you know you definitely have a LBM image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadLBM_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1207</i>
   */
  SDL_Surface IMG_LoadLBM_RW(SDL_RWops src);

  /**
   * Load a PCX image directly.<br>
   * If you know you definitely have a PCX image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadPCX_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1237</i>
   */
  SDL_Surface IMG_LoadPCX_RW(SDL_RWops src);

  /**
   * Load a PNG image directly.<br>
   * If you know you definitely have a PNG image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadPNG_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1267</i>
   */
  SDL_Surface IMG_LoadPNG_RW(SDL_RWops src);

  /**
   * Load a PNM image directly.<br>
   * If you know you definitely have a PNM image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadPNM_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1297</i>
   */
  SDL_Surface IMG_LoadPNM_RW(SDL_RWops src);

  /**
   * Load a SVG image directly.<br>
   * If you know you definitely have a SVG image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.2.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadSVG_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1327</i>
   */
  SDL_Surface IMG_LoadSVG_RW(SDL_RWops src);

  /**
   * Load a QOI image directly.<br>
   * If you know you definitely have a QOI image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadQOI_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1357</i>
   */
  SDL_Surface IMG_LoadQOI_RW(SDL_RWops src);

  /**
   * Load a TGA image directly.<br>
   * If you know you definitely have a TGA image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadTGA_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1387</i>
   */
  SDL_Surface IMG_LoadTGA_RW(SDL_RWops src);

  /**
   * Load a TIFF image directly.<br>
   * If you know you definitely have a TIFF image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadTIF_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1417</i>
   */
  SDL_Surface IMG_LoadTIF_RW(SDL_RWops src);

  /**
   * Load a XCF image directly.<br>
   * If you know you definitely have a XCF image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadXCF_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1447</i>
   */
  SDL_Surface IMG_LoadXCF_RW(SDL_RWops src);

  /**
   * Load a XPM image directly.<br>
   * If you know you definitely have a XPM image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadXPM_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1477</i>
   */
  SDL_Surface IMG_LoadXPM_RW(SDL_RWops src);

  /**
   * Load a XV image directly.<br>
   * If you know you definitely have a XV image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadWEBP_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadXV_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1507</i>
   */
  SDL_Surface IMG_LoadXV_RW(SDL_RWops src);

  /**
   * Load a WEBP image directly.<br>
   * If you know you definitely have a WEBP image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops to load image data from.<br>
   * \returns SDL surface, or NULL on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_LoadAVIF_RW<br>
   * \sa IMG_LoadICO_RW<br>
   * \sa IMG_LoadCUR_RW<br>
   * \sa IMG_LoadBMP_RW<br>
   * \sa IMG_LoadGIF_RW<br>
   * \sa IMG_LoadJPG_RW<br>
   * \sa IMG_LoadJXL_RW<br>
   * \sa IMG_LoadLBM_RW<br>
   * \sa IMG_LoadPCX_RW<br>
   * \sa IMG_LoadPNG_RW<br>
   * \sa IMG_LoadPNM_RW<br>
   * \sa IMG_LoadSVG_RW<br>
   * \sa IMG_LoadQOI_RW<br>
   * \sa IMG_LoadTGA_RW<br>
   * \sa IMG_LoadTIF_RW<br>
   * \sa IMG_LoadXCF_RW<br>
   * \sa IMG_LoadXPM_RW<br>
   * \sa IMG_LoadXV_RW<br>
   * Original signature : <code>SDL_Surface* IMG_LoadWEBP_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1537</i>
   */
  SDL_Surface IMG_LoadWEBP_RW(SDL_RWops src);

  /**
   * Load an SVG image, scaled to a specific size.<br>
   * Since SVG files are resolution-independent, you specify the size you would<br>
   * like the output image to be and it will be generated at those dimensions.<br>
   * Either width or height may be 0 and the image will be auto-sized to<br>
   * preserve aspect ratio.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param src an SDL_RWops to load SVG data from.<br>
   * \param width desired width of the generated surface, in pixels.<br>
   * \param height desired height of the generated surface, in pixels.<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * Original signature : <code>SDL_Surface* IMG_LoadSizedSVG_RW(SDL_RWops*, int, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1553</i>
   */
  SDL_Surface IMG_LoadSizedSVG_RW(SDL_RWops src, int width, int height);

  /**
   * Load an XPM image from a memory array.<br>
   * The returned surface will be an 8bpp indexed surface, if possible,<br>
   * otherwise it will be 32bpp. If you always want 32-bit data, use<br>
   * IMG_ReadXPMFromArrayToRGB888() instead.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param xpm a null-terminated array of strings that comprise XPM data.<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_ReadXPMFromArrayToRGB888<br>
   * Original signature : <code>SDL_Surface* IMG_ReadXPMFromArray(char**)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1567</i>
   */
  SDL_Surface IMG_ReadXPMFromArray(PointerByReference xpm);

  /**
   * Load an XPM image from a memory array.<br>
   * The returned surface will always be a 32-bit RGB surface. If you want 8-bit<br>
   * indexed colors (and the XPM data allows it), use IMG_ReadXPMFromArray()<br>
   * instead.<br>
   * When done with the returned surface, the app should dispose of it with a<br>
   * call to SDL_FreeSurface().<br>
   * \param xpm a null-terminated array of strings that comprise XPM data.<br>
   * \returns a new SDL surface, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_ReadXPMFromArray<br>
   * Original signature : <code>SDL_Surface* IMG_ReadXPMFromArrayToRGB888(char**)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1581</i>
   */
  SDL_Surface IMG_ReadXPMFromArrayToRGB888(PointerByReference xpm);

  /**
   * Save an SDL_Surface into a PNG image file.<br>
   * If the file already exists, it will be overwritten.<br>
   * \param surface the SDL surface to save<br>
   * \param file path on the filesystem to write new file to.<br>
   * \returns 0 if successful, -1 on error<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_SavePNG_RW<br>
   * \sa IMG_SaveJPG<br>
   * \sa IMG_SaveJPG_RW<br>
   * Original signature : <code>int IMG_SavePNG(SDL_Surface*, const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1594</i>
   */
  int IMG_SavePNG(SDL_Surface surface, String file);

  /**
   * Save an SDL_Surface into PNG image data, via an SDL_RWops.<br>
   * If you just want to save to a filename, you can use IMG_SavePNG() instead.<br>
   * \param surface the SDL surface to save<br>
   * \param dst the SDL_RWops to save the image data to.<br>
   * \returns 0 if successful, -1 on error.<br>
   * \since This function is available since SDL_image 2.0.0.<br>
   * \sa IMG_SavePNG<br>
   * \sa IMG_SaveJPG<br>
   * \sa IMG_SaveJPG_RW<br>
   * Original signature : <code>int IMG_SavePNG_RW(SDL_Surface*, SDL_RWops*, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1607</i>
   */
  int IMG_SavePNG_RW(SDL_Surface surface, SDL_RWops dst, int freedst);

  /**
   * Save an SDL_Surface into a JPEG image file.<br>
   * If the file already exists, it will be overwritten.<br>
   * \param surface the SDL surface to save<br>
   * \param file path on the filesystem to write new file to.<br>
   * \param quality [0; 33] is Lowest quality, [34; 66] is Middle quality, [67;<br>
   * 100] is Highest quality<br>
   * \returns 0 if successful, -1 on error<br>
   * \since This function is available since SDL_image 2.0.2.<br>
   * \sa IMG_SaveJPG_RW<br>
   * \sa IMG_SavePNG<br>
   * \sa IMG_SavePNG_RW<br>
   * Original signature : <code>int IMG_SaveJPG(SDL_Surface*, const char*, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1622</i>
   */
  int IMG_SaveJPG(SDL_Surface surface, String file, int quality);

  /**
   * Save an SDL_Surface into JPEG image data, via an SDL_RWops.<br>
   * If you just want to save to a filename, you can use IMG_SaveJPG() instead.<br>
   * \param surface the SDL surface to save<br>
   * \param dst the SDL_RWops to save the image data to.<br>
   * \returns 0 if successful, -1 on error.<br>
   * \since This function is available since SDL_image 2.0.2.<br>
   * \sa IMG_SaveJPG<br>
   * \sa IMG_SavePNG<br>
   * \sa IMG_SavePNG_RW<br>
   * Original signature : <code>int IMG_SaveJPG_RW(SDL_Surface*, SDL_RWops*, int, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1635</i>
   */
  int IMG_SaveJPG_RW(SDL_Surface surface, SDL_RWops dst, int freedst, int quality);

  /**
   * Load an animation from a file.<br>
   * When done with the returned animation, the app should dispose of it with a<br>
   * call to IMG_FreeAnimation().<br>
   * \param file path on the filesystem containing an animated image.<br>
   * \returns a new IMG_Animation, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_FreeAnimation<br>
   * Original signature : <code>IMG_Animation* IMG_LoadAnimation(const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1657</i>
   */
  IMG_Animation IMG_LoadAnimation(String file);

  /**
   * Load an animation from an SDL_RWops.<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * When done with the returned animation, the app should dispose of it with a<br>
   * call to IMG_FreeAnimation().<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \returns a new IMG_Animation, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_FreeAnimation<br>
   * Original signature : <code>IMG_Animation* IMG_LoadAnimation_RW(SDL_RWops*, int)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1673</i>
   */
  IMG_Animation IMG_LoadAnimation_RW(SDL_RWops src, int freesrc);

  /**
   * Load an animation from an SDL datasource<br>
   * Even though this function accepts a file type, SDL_image may still try<br>
   * other decoders that are capable of detecting file type from the contents of<br>
   * the image data, but may rely on the caller-provided type string for formats<br>
   * that it cannot autodetect. If `type` is NULL, SDL_image will rely solely on<br>
   * its ability to guess the format.<br>
   * If `freesrc` is non-zero, the RWops will be closed before returning,<br>
   * whether this function succeeds or not. SDL_image reads everything it needs<br>
   * from the RWops during this call in any case.<br>
   * When done with the returned animation, the app should dispose of it with a<br>
   * call to IMG_FreeAnimation().<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \param freesrc non-zero to close/free the SDL_RWops before returning, zero<br>
   * to leave it open.<br>
   * \param type a filename extension that represent this data ("GIF", etc).<br>
   * \returns a new IMG_Animation, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAnimation<br>
   * \sa IMG_LoadAnimation_RW<br>
   * \sa IMG_FreeAnimation<br>
   * Original signature : <code>
   * IMG_Animation* IMG_LoadAnimationTyped_RW(SDL_RWops*, int, const char*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1697</i>
   */
  IMG_Animation IMG_LoadAnimationTyped_RW(SDL_RWops src, int freesrc, String type);

  /**
   * Dispose of an IMG_Animation and free its resources.<br>
   * The provided `anim` pointer is not valid once this call returns.<br>
   * \param anim IMG_Animation to dispose of.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAnimation<br>
   * \sa IMG_LoadAnimation_RW<br>
   * \sa IMG_LoadAnimationTyped_RW<br>
   * Original signature : <code>void IMG_FreeAnimation(IMG_Animation*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1708</i>
   */
  void IMG_FreeAnimation(IMG_Animation anim);

  /**
   * Load a GIF animation directly.<br>
   * If you know you definitely have a GIF image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \returns a new IMG_Animation, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAnimation<br>
   * \sa IMG_LoadAnimation_RW<br>
   * \sa IMG_LoadAnimationTyped_RW<br>
   * \sa IMG_FreeAnimation<br>
   * Original signature : <code>IMG_Animation* IMG_LoadGIFAnimation_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1724</i>
   */
  IMG_Animation IMG_LoadGIFAnimation_RW(SDL_RWops src);

  /**
   * Load a WEBP animation directly.<br>
   * If you know you definitely have a WEBP image, you can call this function,<br>
   * which will skip SDL_image's file format detection routines. Generally it's<br>
   * better to use the abstract interfaces; also, there is only an SDL_RWops<br>
   * interface available here.<br>
   * \param src an SDL_RWops that data will be read from.<br>
   * \returns a new IMG_Animation, or NULL on error.<br>
   * \since This function is available since SDL_image 2.6.0.<br>
   * \sa IMG_LoadAnimation<br>
   * \sa IMG_LoadAnimation_RW<br>
   * \sa IMG_LoadAnimationTyped_RW<br>
   * \sa IMG_FreeAnimation<br>
   * Original signature : <code>IMG_Animation* IMG_LoadWEBPAnimation_RW(SDL_RWops*)</code><br>
   * <i>native declaration : /usr/local/include/SDL2/SDL_image.h:1740</i>
   */
  IMG_Animation IMG_LoadWEBPAnimation_RW(SDL_RWops src);

  /**
   * Initialization flags<br>
   */
  interface IMG_InitFlags {
    int IMG_INIT_JPG = 0x00000001;
    int IMG_INIT_PNG = 0x00000002;
    int IMG_INIT_TIF = 0x00000004;
    int IMG_INIT_WEBP = 0x00000008;
    int IMG_INIT_JXL = 0x00000010;
    int IMG_INIT_AVIF = 0x00000020;
  }
}
