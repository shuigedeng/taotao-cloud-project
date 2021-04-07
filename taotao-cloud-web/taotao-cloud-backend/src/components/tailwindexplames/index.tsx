import React, { useContext, useEffect, useState } from 'react'
import erin from '/@/assets/images/erin-lindford.jpg'

const Tailwindexplames: React.FC = () => {
  return (
    <div className="md:container md:mx-auto px-4 sm:bg-blue-800 md:bg-green-800">
      <div className="flex flex-row flex-nowrap ">
        <div className="flex-grow bg-red-400 w-16 h-40 order-last">
          hello
        </div>
        <div className="flex-shrink-0 bg-purple-50 w-16 h-20">
          h1
        </div>
        <div className="flex-grow bg-indigo-800 w-16 h-20">
          h2
        </div>
      </div>
    </div>




    // <div className="dark bg-white dark:bg-black py-8 px-8 max-w-sm mx-auto  rounded-xl shadow-md
    //                 space-y-2 sm:py-4 sm:flex sm:items-center sm:space-y-0 sm:space-x-6">
    //   <img
    //     className="block mx-auto h-24 rounded-full sm:mx-0 sm:flex-shrink-0"
    //     src={erin}
    //     alt="Woman's Face"
    //   />
    //   <div className="text-center space-y-2 sm:text-left">
    //     <div className="space-y-0.5">
    //       <p className="text-lg text-black font-semibold">Erin Lindford</p>
    //       <p className="text-gray-500 font-medium">Product Engineer</p>
    //     </div>
    //     <button className="px-4 py-1 text-sm text-purple-600 font-semibold rounded-full
    //                        border border-purple-200 hover:text-white hover:bg-purple-600
    //                        hover:border-transparent focus:outline-none focus:ring-2
    //                        focus:ring-purple-600 focus:ring-offset-2">
    //       Message
    //     </button>
    //
    //     <button className="btn-blue">
    //       button
    //     </button>
    //   </div>
    // </div>
  )
}

export default Tailwindexplames
