import numpy as np
import math

class Map:
    h = 400.0  # Camera focus distance in pixels. Should be calibrated.

    baseX = [1, 0, 0, 0]
    baseY = [0, 1, 0, 0]

    def updatePoint(self, affine, pointVectors):
        startPosition = [0, 0, 0, 1]
        #movement vector
        endPosition = np.dot(affine, startPosition)
        moveVector = np.subtract(endPosition, startPosition)

        #affineT = np.linalg.inv(affine)

        newBaseX = np.dot(affine, self.baseX)
        newBaseY = np.dot(affine, self.baseY)

        baseMoveAngleA = self.angle(moveVector, self.baseY)
        baseMoveAngleB = self.angle(moveVector, newBaseY)

        baseAngleY = self.angle(newBaseY, [1, 0, 0, 0])

        print moveVector
        print baseMoveAngleA, baseMoveAngleB

        for pointVector in pointVectors:
            angleA = baseMoveAngleA + math.atan(self.h / pointVector[0][0])
            angleB = math.pi - baseMoveAngleB - math.atan(self.h / pointVector[1][0])
            print angleA*180/math.pi, angleB*180/math.pi
            distance = math.fabs(self.vectNorm(moveVector) * math.sin(angleA) / math.sin(math.pi - angleA - angleB))
            angleToPoint=angleB-baseAngleY
            x = distance*math.cos(angleToPoint)
            y = distance*math.sin(angleToPoint)

            print [endPosition[0]+x, endPosition[1]+y]

        self.baseX=newBaseX
        self.baseY=newBaseY

    def angle(self, v1, v2):
        v1_u = v1 / self.vectNorm(v1)
        v2_u = v2 / self.vectNorm(v2)
        angle = np.arccos(np.dot(v1_u, v2_u))
        if np.isnan(angle):
            if (v1_u == v2_u):
                return 0.0
            else:
                return np.pi
        return angle


    def vectNorm(self, vect):
        return np.linalg.norm(vect)
        #return math.sqrt(vect[0] ** 2 + vect[1] ** 2 + vect[2] ** 2)


def main():

    #Params

    #point position for each step in pixels. Zero is view axis of the camera
    #pointVector = [[200, 0], [400, 0]]
    pointVector = [[400, 0], [800, 0]]

    #Camera movement
    affine = [[1, 0, 0, 0],
              [0, 1, 0, 1],
              [0, 0, 1, 0],
              [0, 0, 0, 1]]

    Map().updatePoint(affine, [pointVector])

if __name__ == '__main__':
    main()