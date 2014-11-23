package com.domeke.app.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encoder {
	private static final Pattern FORMAT_PATTERN = Pattern
			.compile("^\\s*([D ])([E ])\\s+([\\w,]+)\\s+.+$");
	private static final Pattern ENCODER_DECODER_PATTERN = Pattern.compile(
			"^\\s*([D ])([E ])([AVS]).{3}\\s+(.+)$", 2);
	private static final Pattern PROGRESS_INFO_PATTERN = Pattern.compile(
			"\\s*(\\w+)\\s*=\\s*(\\S+)\\s*", 2);
	private static final Pattern SIZE_PATTERN = Pattern.compile(
			"(\\d+)x(\\d+)", 2);
	private static final Pattern FRAME_RATE_PATTERN = Pattern.compile(
			"([\\d.]+)\\s+(?:fps|tb\\(r\\))", 2);
	private static final Pattern BIT_RATE_PATTERN = Pattern.compile(
			"(\\d+)\\s+kb/s", 2);
	private static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile(
			"(\\d+)\\s+Hz", 2);
	private static final Pattern CHANNELS_PATTERN = Pattern.compile(
			"(mono|stereo)", 2);
	private static final Pattern SUCCESS_PATTERN = Pattern.compile(
			"^\\s*video\\:\\S+\\s+audio\\:\\S+\\s+global headers\\:\\S+.*$", 2);
	private FFMPEGLocator locator;

	public Encoder() {
		this.locator = new DefaultFFMPEGLocator();
	}

	public Encoder(FFMPEGLocator locator) {
		this.locator = locator;
	}

	public String[] getAudioDecoders() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String decoderFlag = matcher.group(1);
					String audioVideoFlag = matcher.group(3);
					if (("D".equals(decoderFlag))
							&& ("A".equals(audioVideoFlag))) {
						String name = matcher.group(4);
						res.add(name);
					}

				} else if (line.trim().equals("Codecs:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public String[] getAudioEncoders() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String encoderFlag = matcher.group(2);
					String audioVideoFlag = matcher.group(3);
					if (("E".equals(encoderFlag))
							&& ("A".equals(audioVideoFlag))) {
						String name = matcher.group(4);
						res.add(name);
					}

				} else if (line.trim().equals("Codecs:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public String[] getVideoDecoders() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String decoderFlag = matcher.group(1);
					String audioVideoFlag = matcher.group(3);
					if (("D".equals(decoderFlag))
							&& ("V".equals(audioVideoFlag))) {
						String name = matcher.group(4);
						res.add(name);
					}

				} else if (line.trim().equals("Codecs:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public String[] getVideoEncoders() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String encoderFlag = matcher.group(2);
					String audioVideoFlag = matcher.group(3);
					if (("E".equals(encoderFlag))
							&& ("V".equals(audioVideoFlag))) {
						String name = matcher.group(4);
						res.add(name);
					}

				} else if (line.trim().equals("Codecs:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public String[] getSupportedEncodingFormats() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = FORMAT_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String encoderFlag = matcher.group(2);
					if ("E".equals(encoderFlag)) {
						String aux = matcher.group(3);
						StringTokenizer st = new StringTokenizer(aux, ",");
						while (st.hasMoreTokens()) {
							String token = st.nextToken().trim();
							if (!(res.contains(token)))
								res.add(token);

						}

					}

				} else if (line.trim().equals("File formats:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public String[] getSupportedDecodingFormats() throws EncoderException {
		ArrayList res = new ArrayList();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-formats");
		try {
			String line;
			ffmpeg.execute();
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getInputStream()));

			boolean evaluate = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;

				if (evaluate) {
					Matcher matcher = FORMAT_PATTERN.matcher(line);
					if (!(matcher.matches()))
						break;
					String decoderFlag = matcher.group(1);
					if ("D".equals(decoderFlag)) {
						String aux = matcher.group(3);
						StringTokenizer st = new StringTokenizer(aux, ",");
						while (st.hasMoreTokens()) {
							String token = st.nextToken().trim();
							if (!(res.contains(token)))
								res.add(token);

						}

					}

				} else if (line.trim().equals("File formats:")) {
					evaluate = true;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		} finally {
			ffmpeg.destroy();
		}
		int size = res.size();
		String[] ret = new String[size];
		for (int i = 0; i < size; ++i)
			ret[i] = ((String) res.get(i));

		return ret;
	}

	public MultimediaInfo getInfo(File source) throws InputFormatException,
			EncoderException {
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		ffmpeg.addArgument("-i");
		ffmpeg.addArgument(source.getAbsolutePath());
		try {
			ffmpeg.execute();
		} catch (IOException e) {
			throw new EncoderException(e);
		}
		try {
			MultimediaInfo localMultimediaInfo;
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getErrorStream()));
			return parseMultimediaInfo(source, reader);
		} finally {
			ffmpeg.destroy();
		}
	}

	private MultimediaInfo parseMultimediaInfo(File source,
			RBufferedReader reader) throws InputFormatException,
			EncoderException {
		Pattern p1 = Pattern.compile("^\\s*Input #0, (\\w+).+$\\s*", 2);
		Pattern p2 = Pattern.compile(
				"^\\s*Duration: (\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d).*$", 2);
		Pattern p3 = Pattern.compile(
				"^\\s*Stream #\\S+: ((?:Audio)|(?:Video)|(?:Data)): (.*)\\s*$",
				2);
		MultimediaInfo info = null;
		try {
			String line;
			int step = 0;
			do {
				line = reader.readLine();
				if (line == null)
					break;

				if (step == 0) {
					String token = source.getAbsolutePath() + ": ";
					if (line.startsWith(token)) {
						String message = line.substring(token.length());
						throw new InputFormatException(message);
					}
					Matcher m = p1.matcher(line);
					if (m.matches()) {
						String format = m.group(1);
						info = new MultimediaInfo();
						info.setFormat(format);
						++step;
					}
				} else {
					Matcher m;
					if (step == 1) {
						m = p2.matcher(line);
						if (m.matches()) {
							long hours = Integer.parseInt(m.group(1));
							long minutes = Integer.parseInt(m.group(2));
							long seconds = Integer.parseInt(m.group(3));
							long dec = Integer.parseInt(m.group(4));
							long duration = dec * 100L + seconds * 1000L
									+ minutes * 60L * 1000L + hours * 60L * 60L
									* 1000L;
							info.setDuration(duration);
							++step;
						} else {
							step = 3;
						}
					} else if (step == 2) {
						m = p3.matcher(line);
						if (m.matches()) {
							StringTokenizer st;
							int i;
							String token;
							boolean parsed;
							Matcher m2;
							int bitRate;
							String type = m.group(1);
							String specs = m.group(2);
							if ("Video".equalsIgnoreCase(type)) {
								VideoInfo video = new VideoInfo();
								st = new StringTokenizer(specs, ",");
								for (i = 0; st.hasMoreTokens(); ++i) {
									token = st.nextToken().trim();
									if (i == 0) {
										video.setDecoder(token);
									} else {
										parsed = false;

										m2 = SIZE_PATTERN.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											int width = Integer.parseInt(m2
													.group(1));
											int height = Integer.parseInt(m2
													.group(2));
											video.setSize(new VideoSize(width,
													height));
											parsed = true;
										}

										m2 = FRAME_RATE_PATTERN.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											try {
												float frameRate = Float
														.parseFloat(m2.group(1));
												video.setFrameRate(frameRate);
											} catch (java.lang.NumberFormatException frameRate) {
											}
											parsed = true;
										}

										m2 = BIT_RATE_PATTERN.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											bitRate = Integer.parseInt(m2
													.group(1));
											video.setBitRate(bitRate);
											parsed = true;
										}
									}
								}
								info.setVideo(video);
							} else if ("Audio".equalsIgnoreCase(type)) {
								AudioInfo audio = new AudioInfo();
								st = new StringTokenizer(specs, ",");
								for (i = 0; st.hasMoreTokens(); ++i) {
									token = st.nextToken().trim();
									if (i == 0) {
										audio.setDecoder(token);
									} else {
										parsed = false;

										m2 = SAMPLING_RATE_PATTERN
												.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											int samplingRate = Integer
													.parseInt(m2.group(1));
											audio.setSamplingRate(samplingRate);
											parsed = true;
										}

										m2 = CHANNELS_PATTERN.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											String ms = m2.group(1);
											if ("mono".equalsIgnoreCase(ms)) {
												audio.setChannels(1);
											} else if ("stereo"
													.equalsIgnoreCase(ms))
												audio.setChannels(2);

											parsed = true;
										}

										m2 = BIT_RATE_PATTERN.matcher(token);
										if ((!(parsed)) && (m2.find())) {
											bitRate = Integer.parseInt(m2
													.group(1));
											audio.setBitRate(bitRate);
											parsed = true;
										}
									}
								}
								info.setAudio(audio);
							}
						} else {
							step = 3;
						}
					}
				}
			} while (step != 3);
			reader.reinsertLine(line);
		} catch (IOException e) {
			throw new EncoderException(e);
		}
		if (info == null)
			label843: throw new InputFormatException();

		return info;
	}

	private Hashtable parseProgressInfoLine(String line) {
		Hashtable table = null;
		Matcher m = PROGRESS_INFO_PATTERN.matcher(line);
		while (m.find()) {
			if (table == null)
				table = new Hashtable();

			String key = m.group(1);
			String value = m.group(2);
			table.put(key, value);
		}
		return table;
	}

	public void encode(File source, File target, EncodingAttributes attributes)
			throws IllegalArgumentException, InputFormatException,
			EncoderException {
		encode(source, target, attributes, null);
	}

	public void encode(File source, File target, EncodingAttributes attributes,
			EncoderProgressListener listener) throws IllegalArgumentException,
			InputFormatException, EncoderException {
		String codec;
		String formatAttribute = attributes.getFormat();
		Float offsetAttribute = attributes.getOffset();
		Float durationAttribute = attributes.getDuration();
		AudioAttributes audioAttributes = attributes.getAudioAttributes();
		VideoAttributes videoAttributes = attributes.getVideoAttributes();
		if ((audioAttributes == null) && (videoAttributes == null))
			throw new IllegalArgumentException(
					"Both audio and video attributes are null");

		target = target.getAbsoluteFile();
		target.getParentFile().mkdirs();
		FFMPEGExecutor ffmpeg = this.locator.createExecutor();
		if (offsetAttribute != null) {
			ffmpeg.addArgument("-ss");
			ffmpeg.addArgument(String.valueOf(offsetAttribute.floatValue()));
		}
		ffmpeg.addArgument("-i");
		ffmpeg.addArgument(source.getAbsolutePath());
		if (durationAttribute != null) {
			ffmpeg.addArgument("-t");
			ffmpeg.addArgument(String.valueOf(durationAttribute.floatValue()));
		}
		if (videoAttributes == null) {
			ffmpeg.addArgument("-vn");
		} else {
			codec = videoAttributes.getCodec();
			if (codec != null) {
				ffmpeg.addArgument("-vcodec");
				ffmpeg.addArgument(codec);
			}
			String tag = videoAttributes.getTag();
			if (tag != null) {
				ffmpeg.addArgument("-vtag");
				ffmpeg.addArgument(tag);
			}
			Integer bitRate = videoAttributes.getBitRate();
			if (bitRate != null) {
				ffmpeg.addArgument("-b");
				ffmpeg.addArgument(String.valueOf(bitRate.intValue()));
			}
			Integer frameRate = videoAttributes.getFrameRate();
			if (frameRate != null) {
				ffmpeg.addArgument("-r");
				ffmpeg.addArgument(String.valueOf(frameRate.intValue()));
			}
			VideoSize size = videoAttributes.getSize();
			if (size != null) {
				ffmpeg.addArgument("-s");
				ffmpeg.addArgument(String.valueOf(size.getWidth()) + "x"
						+ String.valueOf(size.getHeight()));
			}
		}
		if (audioAttributes == null) {
			ffmpeg.addArgument("-an");
		} else {
			codec = audioAttributes.getCodec();
			if (codec != null) {
				ffmpeg.addArgument("-acodec");
				ffmpeg.addArgument(codec);
			}
			Integer bitRate = audioAttributes.getBitRate();
			if (bitRate != null) {
				ffmpeg.addArgument("-ab");
				ffmpeg.addArgument(String.valueOf(bitRate.intValue()));
			}
			Integer channels = audioAttributes.getChannels();
			if (channels != null) {
				ffmpeg.addArgument("-ac");
				ffmpeg.addArgument(String.valueOf(channels.intValue()));
			}
			Integer samplingRate = audioAttributes.getSamplingRate();
			if (samplingRate != null) {
				ffmpeg.addArgument("-ar");
				ffmpeg.addArgument(String.valueOf(samplingRate.intValue()));
			}
			Integer volume = audioAttributes.getVolume();
			if (volume != null) {
				ffmpeg.addArgument("-vol");
				ffmpeg.addArgument(String.valueOf(volume.intValue()));
			}
		}
		ffmpeg.addArgument("-f");
		ffmpeg.addArgument(formatAttribute);
		ffmpeg.addArgument("-y");
		ffmpeg.addArgument(target.getAbsolutePath());
		try {
			ffmpeg.execute();
		} catch (IOException e) {
			throw new EncoderException(e);
		}
		try {
			long duration;
			String line;
			String lastWarning = null;

			long progress = -929652156619816960L;
			RBufferedReader reader = null;
			reader = new RBufferedReader(new InputStreamReader(
					ffmpeg.getErrorStream()));
			MultimediaInfo info = parseMultimediaInfo(source, reader);
			if (durationAttribute != null) {
				duration = Math.round(durationAttribute.floatValue() * 1000.0F);
			} else {
				duration = info.getDuration();
				if (offsetAttribute != null) {
					duration = duration
							- Math.round(offsetAttribute.floatValue() * 1000.0F);
				}
			}
			if (listener != null)
				listener.sourceInfo(info);

			int step = 0;

			while ((line = reader.readLine()) != null) {
				if (step == 0)
					if (line.startsWith("WARNING: ")) {
						if (listener != null)
							listener.message(line);
					} else {
						if (!(line.startsWith("Output #0")))
							throw new EncoderException(line);

						++step;
					}
				else if ((step == 1) && (!(line.startsWith("  ")))) {
					++step;
				}

				if (step == 2) {
					if (!(line.startsWith("Stream mapping:")))
						throw new EncoderException(line);

					++step;
				} else if ((step == 3) && (!(line.startsWith("  ")))) {
					++step;
				}

				if (step == 4) {
					line = line.trim();
					if (line.length() > 0) {
						Hashtable table = parseProgressInfoLine(line);
						if (table == null) {
							if (listener != null)
								listener.message(line);

							lastWarning = line;
						} else {
							if (listener != null) {
								String time = (String) table.get("time");
								if (time != null) {
									int dot = time.indexOf(46);
									if ((dot > 0)
											&& (dot == time.length() - 2)
											&& (duration > -929646023406518272L)) {
										String p1 = time.substring(0, dot);
										String p2 = time.substring(dot + 1);
										try {
											long i1 = Long.parseLong(p1);
											long i2 = Long.parseLong(p2);
											progress = i1 * 1000L + i2 * 100L;
											int perm = (int) Math
													.round(progress * 1000L
															/ duration);
											if (perm > 1000)
												perm = 1000;

											listener.progress(perm);
										} catch (java.lang.NumberFormatException i1) {
										}
									}
								}
							}
							lastWarning = null;
						}
					}
				}
			}
			if (lastWarning == null)
				throw new EncoderException(lastWarning);
		} catch (IOException lastWarning) {
		} finally {
			ffmpeg.destroy();
		}
	}
}