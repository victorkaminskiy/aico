#!/usr/bin/env python

'''
Lucas-Kanade tracker
====================

Lucas-Kanade sparse optical flow demo. Uses goodFeaturesToTrack
for track initialization and back-tracking for match verification
between frames.

Usage
-----
lk_track.py [<video_source>]


Keys
----
ESC - exit
'''

import numpy as np
import cv2
import video
import time
from common import anorm2, draw_str

lk_params = dict( winSize  = (15, 15),
                  maxLevel = 2,
                  criteria = (cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 10, 0.03))

feature_params = dict( maxCorners = 10,
                       qualityLevel = 0.3,
                       minDistance = 7,
                       blockSize = 7 )

class App:
    def __init__(self, video_src):
        self.track_len = 10
        self.detect_interval = 3
        self.tracks = {}
        self.cam = video.create_capture(video_src)
        self.frame_idx = 0
        self.prev_gray = {}

    def run(self):
        while True:
            ret, frame = self.cam.read()
            frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            vis = frame.copy()
            xBlocksCount=5;
            yBlocksCount=5;
            rows,cols=frame_gray.shape
            xBlockSize = cols/xBlocksCount
            yBlockSize = rows/yBlocksCount

            for i in range(0,xBlocksCount*yBlocksCount):
                if i not in self.tracks:
                    self.tracks[i] = []
                xBlock=i % xBlocksCount
                yBlock=i / xBlocksCount

                imageToProcess=frame_gray[yBlock*yBlockSize:(yBlock+1)*yBlockSize,xBlock*xBlockSize:(xBlock+1)*xBlockSize]

                if len(self.tracks[i]) > 0:
                    img0, img1 = self.prev_gray[i], imageToProcess
                    p0 = np.float32([tr[-1] for tr in self.tracks[i]]).reshape(-1, 1, 2)
                    p1, st, err = cv2.calcOpticalFlowPyrLK(img0, img1, p0, None, **lk_params)
                    p0r, st, err = cv2.calcOpticalFlowPyrLK(img1, img0, p1, None, **lk_params)
                    d = abs(p0-p0r).reshape(-1, 2).max(-1)
                    good = d < 1
                    new_tracks = []
                    for tr, (x, y), good_flag in zip(self.tracks[i], p1.reshape(-1, 2), good):
                        if not good_flag:
                            continue
                        tr.append((x, y))
                        if len(tr) > self.track_len:
                            del tr[0]
                        new_tracks.append(tr)
                        cv2.circle(vis, (int(x+xBlock*xBlockSize), int(y+yBlock*yBlockSize)), 2, (0, 255, 0), -1)
                    self.tracks[i] = new_tracks
                    paintTrack=[]
                    for tr in self.tracks[i]:
                        new_tr=[]
                        for point in tr:
                            new_tr.append((point[0]+xBlock*xBlockSize,point[1]+yBlock*yBlockSize))
                        paintTrack.append(np.int32(new_tr))
                    cv2.polylines(vis, paintTrack, False, (0, 255, 0))
                draw_str(vis, (20, 20+i*20), 'track count(%d): %d %d %d' % (i, len(self.tracks[i]), xBlock, yBlock))
                cv2.line(vis,(xBlock*xBlockSize,0),(xBlock*xBlockSize,rows),(0,0,255))
                cv2.line(vis,(0,yBlock*yBlockSize),(cols,yBlock*yBlockSize),(0,0,255))
                    # time.sleep(0.1)
                if self.frame_idx % self.detect_interval == 0:
                    mask = np.zeros_like(imageToProcess)
                    mask[:] = 255
                    for x, y in [np.int32(tr[-1]) for tr in self.tracks[i]]:
                        cv2.circle(mask, (x, y), 5, 0, -1)
                    p = cv2.goodFeaturesToTrack(imageToProcess, mask = mask, **feature_params)
                    if p is not None:
                        for x, y in np.float32(p).reshape(-1, 2):
                            self.tracks[i].append([(x, y)])
                self.prev_gray[i] = imageToProcess


            self.frame_idx += 1
            cv2.imshow('lk_track', vis)
            # cv2.imshow('lk_track', self.prev_gray[12])
            time.sleep(0.1)
            ch = 0xFF & cv2.waitKey(1)
            if ch == 27:
                break

def main():
    import sys
    try: video_src = sys.argv[1]
    except: video_src = 0

    print __doc__
    App(video_src).run()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
