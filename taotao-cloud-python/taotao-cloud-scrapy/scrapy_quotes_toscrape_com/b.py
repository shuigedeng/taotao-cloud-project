import tensorflow as tf
import os
import numpy as np
import matplotlib.pyplot as plt

BATCH_SIZE = 2
CAPACITY = 256
IMG_W = 208
IMG_H = 208

train_dir = '/home/dengtao/tarindata'

class PicTrain(object):
    def __init__(self):
        self.cats = []
        self.label_cats = []
        self.dogs = []
        self.label_dogs = []

    def get_files(self, file_dir):
        for file in os.listdir(file_dir+'/Cat'):
                self.cats.append(file_dir +'/Cat'+'/'+ file)
                self.label_cats.append(0)
        for file in os.listdir(file_dir+'/Dog'):
                self.dogs.append(file_dir +'/Dog'+'/'+file)
                self.label_dogs.append(1)

        image_list = np.hstack((self.cats, self.dogs))
        label_list = np.hstack((self.label_cats, self.label_dogs))

        temp = np.array([image_list, label_list])
        temp = temp.transpose()
        np.random.shuffle(temp)

        image_list = list(temp[:, 0])
        label_list = list(temp[:, 1])
        label_list = [int(i) for i in label_list]

        return image_list, label_list

    def get_batch(self, image, label, image_W, image_H, batch_size, capacity):
        image = tf.cast(image, tf.string)
        label = tf.cast(label, tf.int64)

        input_queue = tf.train.slice_input_producer([image, label])

        label = input_queue[1]
        image_contents = tf.read_file(input_queue[0])

        image = tf.image.decode_jpeg(image_contents, channels=3)

        image = tf.image.resize_image_with_crop_or_pad(image, image_W, image_H)

        image = tf.image.per_image_standardization(image)

        image_batch, label_batch = tf.train.batch([image, label],
                                                  batch_size=batch_size,
                                                  num_threads=32,
                                                  capacity=capacity)
        label_batch = tf.reshape(label_batch, [batch_size])
        image_batch = tf.cast(image_batch, tf.float32)

        return label_batch, image_batch



def main():
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

    pt = PicTrain()
    image_list, label_list = pt.get_files(train_dir)
    image_batch, label_batch = pt.get_batch(image_list, label_list, IMG_W, IMG_H, BATCH_SIZE, CAPACITY)

    with tf.Session() as sess:
        i = 0
        coord = tf.train.Coordinator()
        threads = tf.train.start_queue_runners(coord=coord)

        try:
            while not coord.should_stop() and i < 2:

                img, label = sess.run([image_batch, label_batch])

                for j in np.arange(BATCH_SIZE):
                    plt.imshow(label[j,:,:,:])
                    plt.show()
                i += 1

        except tf.errors.OutOfRangeError:
            print('done!')
        finally:
            coord.request_stop()
        coord.join(threads)

if __name__ == '__main__':
    main()